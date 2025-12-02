package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.user.domain.User;
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
     * 결제를 생성한다
     */
    public static Payment create(Order order, User user, LocalDateTime paidAt) {
        // 주문 상태 확인
        if(order.getStatus() != OrderStatus.PENDING){
            throw new BusinessException.InvalidOrderStatusException();
        }
        // 주문자 동일한지 확인
        if(!order.getUser().equals(user)){
            throw new BusinessException.UnauthorizedPaymentException();
        }
        return new Payment(order, PaymentStatus.SUCCESS, order.getTotalAmount(), paidAt);
    }

}
