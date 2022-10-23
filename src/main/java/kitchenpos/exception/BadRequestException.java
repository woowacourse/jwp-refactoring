package kitchenpos.exception;

public class BadRequestException extends RuntimeException {

    private BadRequestCode badRequestCode = BadRequestCode.findByClass(this.getClass());

    private int code;
    private String message;

    public BadRequestException() {
        this.code = badRequestCode.getCode();
        this.message = badRequestCode.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
