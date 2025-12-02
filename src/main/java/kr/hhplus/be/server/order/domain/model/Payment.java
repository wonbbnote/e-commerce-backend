package kr.hhplus.be.server.order.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Payment {

    private Long id;
    private Order order;
    private PaymentStatus status;

    private Integer paymentAmount;  // 결제 금액
    private LocalDateTime paidAt;   // 결제 완료 시각

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 결제 생성용 생성자
    public Payment(Order order, PaymentStatus status,
                   Integer paymentAmount, LocalDateTime paidAt) {
        this.order = order;
        this.status = status;
        this.paymentAmount = paymentAmount;
        this.paidAt = paidAt;
        this.createdAt = paidAt;
        this.updatedAt = paidAt;
    }

    // Mapper용 생성자 (DB에서 조회한 데이터로 객체 재구성)
    public Payment(Long id, Order order, PaymentStatus status,
                   Integer paymentAmount, LocalDateTime paidAt,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.order = order;
        this.status = status;
        this.paymentAmount = paymentAmount;
        this.paidAt = paidAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 결제를 완료한다
     */
    public void success() {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Only pending payments can be completed");
        }
        this.status = PaymentStatus.SUCCESS;
        this.paidAt = LocalDateTime.now();
    }

    /**
     * 결제를 실패한다
     */
    public void fail() {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Only pending payments can be failed");
        }
        this.status = PaymentStatus.FAILED;
    }

    /**
     * 결제를 취소한다
     */
    public void cancel() {
        if (this.status == PaymentStatus.CANCELLED) {
            throw new IllegalArgumentException("Payment already cancelled");
        }
        this.status = PaymentStatus.CANCELLED;
    }

    /**
     * 결제가 성공했는지 확인
     */
    public boolean isSucceeded() {
        return this.status == PaymentStatus.SUCCESS;
    }

    /**
     * 결제를 생성한다 (PENDING 상태)
     */
    public static Payment create(Order order, Integer paymentAmount) {
        return new Payment(order, PaymentStatus.PENDING, paymentAmount, null);
    }

    // Mapper용 Setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
