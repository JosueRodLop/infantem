package com.isppG8.infantem.infantem.subscription;

import com.isppG8.infantem.infantem.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "subscription_table")
@Getter
@Setter
public class SubscriptionInfantem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String stripeSubscriptionId;
    private String stripeCustomerId;
    private LocalDate startDate;
    private boolean active;

}
