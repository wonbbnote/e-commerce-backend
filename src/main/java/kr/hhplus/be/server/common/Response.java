package kr.hhplus.be.server.common;

public record Response<T>(Integer code, String message, T data) {

    public static <T> Response<T> ok(T data){
        return new Response<>(200, "요청이 성공적으로 처리되었습니다.", data);
    }

    public static <T> Response<T> ok(T data, String message){
        return new Response<>(200, message, data);
    }

    public static <T> Response<T> error(ErrorCode error){
        return new Response<>(error.getCode(), error.getMessage(), null);
    }

}
