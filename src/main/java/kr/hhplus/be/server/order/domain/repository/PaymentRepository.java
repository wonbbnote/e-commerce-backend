package kr.hhplus.be.server.order.domain.repository;

import kr.hhplus.be.server.order.domain.model.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
}
