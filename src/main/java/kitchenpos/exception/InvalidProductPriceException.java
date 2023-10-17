package kitchenpos.exception;

public class InvalidProductPriceException extends RuntimeException{
    private final static String error = "잘못된 상품 가격입니다.";
    public InvalidProductPriceException() {
        super(error);
    }
}
