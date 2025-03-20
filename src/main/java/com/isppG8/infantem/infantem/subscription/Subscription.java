package com.isppG8.infantem.infantem.subscription;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.isppG8.infantem.infantem.payment.Payment;
import com.isppG8.infantem.infantem.user.User;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subcription_table")
@Getter
@Setter
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Positive
    private Double price;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private Boolean active;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public LocalDate getNextBillingDate() {
        LocalDate today = LocalDate.now();
        long periodsPassed = ChronoUnit.DAYS.between(startDate, today) / 28;
        return startDate.plusDays((periodsPassed + 1) * 28);
    }

}
