import React, { createContext, useState, useContext, useEffect } from 'react';
import { router } from 'expo-router';
import { getToken, removeToken } from '../utils/jwtStorage';
import { User, AuthContextType } from '../types';

const AuthContext = createContext<AuthContextType>({
  user: null,
  isLoading: true,
  isAuthenticated: false,
  setUser: () => {},
  signOut: async () => {},
  checkAuth: async () => false,
});

// This is our custom hook to use the auth context. Just an easier use
export const useAuth = () => useContext(AuthContext);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const apiUrl = process.env.EXPO_PUBLIC_API_URL;

  useEffect(() => {
    checkAuth();
  }, []);

  const checkAuth = async (): Promise<boolean> => {
      try {
        setIsLoading(true);
        const token = await getToken();
        
        if (!token) {
          setIsAuthenticated(false);
          setUser(null);
          setIsLoading(false);
          return false;
        }

        try {
          const response = await fetch(`${apiUrl}/api/v1/auth/me`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${token}`,
            },
          });

          if (response.ok) {
            const userData = await response.json();
            setUser({
              name: userData.name,
              surname: userData.name,
              username: userData.username,
              email: userData.email 
            });
            setIsAuthenticated(true);
            setIsLoading(false);
            return true;
          } else {
            await signOut();
            return false;
          }

        } catch (error) {
          console.error('Error validating token:', error);
          // I've read some approaches sayn that maybe we should consider validate
          // just via token if the API is not available. ATM I thought it's not a good
          // idea. I return false. - Javier Santos
          setIsAuthenticated(false);
          setUser(null);
          setIsLoading(false);
          return false;
        }
      } catch (error) {
        console.error('Auth check failed:', error);
        setIsAuthenticated(false);
        setUser(null);
        setIsLoading(false);
        return false;
      }
    };

    const signOut = async () => {
      try {
        await removeToken();
        setUser(null);
        setIsAuthenticated(false);
        router.replace('/signin');

      } catch (error) {
        console.error('Sign out failed', error);
      }
    };

    const value = {
      user,
      isLoading,
      isAuthenticated,
      setUser,
      signOut,
      checkAuth,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
