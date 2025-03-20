package com.isppG8.infantem.infantem.subscription;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.isppG8.infantem.infantem.user.User;
import org.springframework.lang.NonNull;
import java.time.LocalDate;

@Entity
@Table(name = "subscription_table")
@Getter
@Setter
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String stripeSubscriptionId;

    @NonNull
    private String stripeCustomerId;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private Boolean active;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
