package kitchenpos.domain.exception;

public class InvalidMenuPriceException extends IllegalArgumentException {

    public InvalidMenuPriceException() {
        super("메뉴의 가격은 메뉴 상품의 총 합보다 같거나 작아야 합니다.");
    }
}
