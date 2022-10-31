package kitchenpos.exceptions;

public class InvalidMenuPriceException extends RuntimeException {

    public InvalidMenuPriceException() {
        super("메뉴 가격이 올바르지 않습니다.");
    }
}
