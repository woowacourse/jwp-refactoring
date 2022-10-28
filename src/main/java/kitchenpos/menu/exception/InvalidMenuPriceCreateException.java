package kitchenpos.menu.exception;

public class InvalidMenuPriceCreateException extends RuntimeException {

    public InvalidMenuPriceCreateException() {
        super("올바르지 않은 메뉴 가격입니다.");
    }
}
