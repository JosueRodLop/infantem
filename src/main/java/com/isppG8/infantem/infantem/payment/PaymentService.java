package com.isppG8.infantem.infantem.payment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${encryption.secret-key}")
    private String secretKey;

    public List<Payment> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        payments.forEach(payment -> payment.decryptData(secretKey)); // Desencripta antes de devolverlos
        return payments;
    }

    public Optional<Payment> getPaymentById(long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        payment.ifPresent(p -> p.decryptData(secretKey)); // Desencripta antes de devolver
        return payment;
    }

    public void create(Payment payment) {
        payment.encryptData(secretKey); // Encripta antes de guardar
        paymentRepository.save(payment);
    }

    public Payment update(long id, Payment payment) {
        Payment paymentToUpdate = paymentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        paymentToUpdate.setIsDefault(payment.getIsDefault());
        paymentToUpdate.setPaymentType(payment.getPaymentType());
        paymentToUpdate.setBillingAgreementId(payment.getBillingAgreementId());
        paymentToUpdate.setPaypalEmail(payment.getPaypalEmail());

        paymentToUpdate.encryptData(secretKey); // Encripta antes de guardar
        return paymentRepository.save(paymentToUpdate);
    }

    public void delete(long id) {
        paymentRepository.deleteById(id);
    }
}
