package kr.hhplus.be.server.order.domain.model;

public enum OrderStatus {
    CONFIRMED, // 주문 완료
    PENDING, // 결제 대기
    PAID, // 결제 완료
    SHIPPED, // 배송 중
    DELIVERED, // 배송 완료
    CANCELLED // 주문 취소
}
