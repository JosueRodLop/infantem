package com.isppG8.infantem.infantem.payment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.isppG8.infantem.infantem.user.User;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "payment_table")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType; // CARD, OTHER

    @NonNull
    private String stripeCustomerId; // ID del cliente en Stripe

    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
