package kitchenpos.exception;

public class InvalidPriceException extends RuntimeException {
    private final static String error = "잘못된 가격입니다.";
    public InvalidPriceException() {
        super(error);
    }
}
