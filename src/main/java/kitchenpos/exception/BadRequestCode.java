package kitchenpos.exception;

import java.util.Arrays;

public enum BadRequestCode {

    NOT_FOUND_ERROR_CODE(0000, "해당 에러 코드를 찾을 수 없습니다.", NotFoundErrorCodeException.class);

    private int code;
    private String message;
    private Class<? extends BadRequestException> type;

    BadRequestCode(final int code, final String message, final Class<? extends BadRequestException> type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public static BadRequestCode findByClass(Class<?> type) {
        return Arrays.stream(BadRequestCode.values())
            .filter(code -> code.type.equals(type))
            .findAny()
            .orElseThrow(NotFoundErrorCodeException::new);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
