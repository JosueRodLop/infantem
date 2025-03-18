package com.isppG8.infantem.infantem.payment;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Agreement;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Plan;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(long id) {
        return paymentRepository.findById(id);
    }

    public void create(Payment payment) {
        paymentRepository.save(payment);
    }

    public Payment update(long id, Payment payment) {
        Payment paymentToUpdate = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        paymentToUpdate.setIsDefault(payment.getIsDefault());
        paymentToUpdate.setPaymentType(payment.getPaymentType());
        paymentToUpdate.setBillingAgreementId(payment.getBillingAgreementId());
        paymentToUpdate.setPaypalEmail(payment.getPaypalEmail());
        return paymentRepository.save(payment);
    }

    public void delete(long id) {
        paymentRepository.deleteById(id);
    }
    


}
