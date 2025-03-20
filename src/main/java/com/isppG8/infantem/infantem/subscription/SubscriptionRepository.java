package com.isppG8.infantem.infantem.subscription;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.isppG8.infantem.infantem.user.User;
import java.time.LocalDate;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.startDate >= :firstDayOfMonth AND s.startDate < :nextMonth")
    List<Subscription> findSubscriptionsExpiringThisMonth(@Param("firstDayOfMonth") LocalDate firstDayOfMonth,
            @Param("nextMonth") LocalDate nextMonth);

    Optional<Subscription> findByUserAndActiveTrue(User user);
}
