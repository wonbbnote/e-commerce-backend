package kr.hhplus.be.server.order.domain.model;

public enum PaymentStatus {
    PENDING,    // 대기 중
    SUCCESS,    // 결제 완료
    FAILED,     // 결제 실패
    CANCELLED   // 취소됨
}
