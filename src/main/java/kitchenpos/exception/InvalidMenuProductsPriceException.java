package kitchenpos.exception;

public class InvalidMenuProductsPriceException extends KitchenPosException {

    private static final String INVALID_MENU_PRODUCTS_PRICE = "메뉴는 단일 상품가격의 합보다 작아야합니다.";

    public InvalidMenuProductsPriceException() {
        super(INVALID_MENU_PRODUCTS_PRICE);
    }
}
