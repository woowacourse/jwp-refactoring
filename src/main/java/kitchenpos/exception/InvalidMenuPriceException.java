package kitchenpos.exception;

public class InvalidMenuPriceException extends RuntimeException {
    private final static String error = "잘못된 메뉴 가격입니다.";
    public InvalidMenuPriceException() {
        super(error);
    }
}
