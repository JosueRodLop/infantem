import React, { useState } from 'react';
import { 
  Modal, 
  View, 
  Text, 
  ScrollView, 
  TouchableOpacity, 
  StyleSheet 
} from 'react-native';

const TermsConditionsModal = ({ 
  visible, 
  onClose, 
  onAccept, 
  onDecline 
}) => {
  const [modalVisible, setModalVisible] = useState(false);

  const termsContent = `
1. Acceptance of Terms
By accessing and using this application, you agree to be bound by these Terms and Conditions. If you do not agree to these terms, please do not use the application.

2. User Eligibility
You must be at least 18 years old to use this application. By using the app, you represent and warrant that you meet this age requirement.

3. User Account
- You are responsible for maintaining the confidentiality of your account
- You agree to accept responsibility for all activities that occur under your account
- You must provide accurate and complete information during registration

4. Privacy
We are committed to protecting your privacy. Please review our Privacy Policy to understand our practices.

5. Intellectual Property
All content, features, and functionality are and will remain the exclusive property of [Company Name] and are protected by international copyright, trademark, patent, trade secret, and other intellectual property laws.

6. Limitation of Liability
- The application is provided "as is" without any representations or warranties
- We shall not be liable for any damages arising from the use of this application
- Our total liability is limited to the maximum extent permitted by law

7. Modifications to Terms
We reserve the right to modify these Terms and Conditions at any time. Continued use of the application after changes constitutes acceptance of the new terms.

8. Governing Law
These Terms and Conditions are governed by and construed in accordance with the laws of [Your Jurisdiction].

9. Contact Information
If you have any questions about these Terms, please contact us at [contact email/address].
`;

return (
    <Modal
      animationType="slide"
      transparent={true}
      visible={visible}
      onRequestClose={onClose}
    >
      <View style={styles.centeredView}>
        <View style={styles.modalView}>
          <Text style={styles.modalTitle}>Terms and Conditions</Text>
          
          <ScrollView style={styles.scrollView}>
            <Text style={styles.termsText}>
              {termsContent}
            </Text>
          </ScrollView>
          
          <View style={styles.buttonContainer}>
            <TouchableOpacity
              style={[styles.button, styles.buttonDecline]}
              onPress={onDecline}
            >
              <Text style={styles.textStyle}>Decline</Text>
            </TouchableOpacity>
            
            <TouchableOpacity
              style={[styles.button, styles.buttonAccept]}
              onPress={onAccept}
            >
              <Text style={styles.textStyle}>Accept</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  centeredView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.5)'
  },
  modalView: {
    width: '90%',
    backgroundColor: 'white',
    borderRadius: 20,
    padding: 20,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
    maxHeight: '80%'
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 15,
    textAlign: 'center'
  },
  scrollView: {
    width: '100%',
    marginBottom: 15
  },
  termsText: {
    fontSize: 14,
    lineHeight: 22
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '100%'
  },
  button: {
    borderRadius: 10,
    padding: 10,
    elevation: 2,
    width: '48%'
  },
  buttonDecline: {
    backgroundColor: '#f44336'
  },
  buttonAccept: {
    backgroundColor: '#4CAF50'
  },
  textStyle: {
    color: 'white',
    fontWeight: 'bold',
    textAlign: 'center'
  },
  openButton: {
    backgroundColor: '#2196F3',
    borderRadius: 10,
    padding: 10,
    elevation: 2
  }
});

export default TermsConditionsModal;
