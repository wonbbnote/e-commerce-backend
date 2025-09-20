package kr.hhplus.be.server.common;

public record Response<T>(Boolean success, String message, T data) {

    public static <T> Response<T> ok(T data){
        return new Response<>(true, "요청이 성공적으로 처리되었습니다.", data);
    }

    public static <T> Response<T> ok(T data, String message){
        return new Response<>(true, message, data);
    }

}
