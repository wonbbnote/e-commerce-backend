package kr.hhplus.be.server.order.domain.model;

public enum OrderStatus {
    PENDING, // 주문 완료 및 결제 대기
    PAID, // 결제 완료
    SHIPPED, // 배송 중
    DELIVERED, // 배송 완료
    CANCELLED // 주문 취소
}
