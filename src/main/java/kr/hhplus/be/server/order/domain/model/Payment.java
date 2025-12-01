package kr.hhplus.be.server.order.domain.model;

import java.time.LocalDateTime;

public class Payment {

    private Long id;
    private Order order;
    private PaymentStatus status;

    private Integer paymentAmount;  // 결제 금액
    private LocalDateTime paidAt;   // 결제 완료 시각

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


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
}
