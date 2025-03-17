package com.isppG8.infantem.infantem.payment;

import com.isppG8.infantem.infantem.user.User;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import org.jasypt.util.text.StrongTextEncryptor;
import java.sql.Types;
import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "payment_table")
@Getter @Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;  // PAYPAL, CARD

    @JdbcTypeCode(Types.VARCHAR)
    private String paypalEmail;

    @JdbcTypeCode(Types.VARCHAR)
    private String billingAgreementId;

    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Método para cifrar datos antes de guardarlos
    public void encryptData(String secretKey) {
        StrongTextEncryptor encryptor = new StrongTextEncryptor();
        encryptor.setPassword(secretKey);
        this.paypalEmail = encryptor.encrypt(this.paypalEmail);
        this.billingAgreementId = encryptor.encrypt(this.billingAgreementId);
    }

    // Método para descifrar datos cuando se leen
    public void decryptData(String secretKey) {
        StrongTextEncryptor encryptor = new StrongTextEncryptor();
        encryptor.setPassword(secretKey);
        this.paypalEmail = encryptor.decrypt(this.paypalEmail);
        this.billingAgreementId = encryptor.decrypt(this.billingAgreementId);
    }
}