package kitchenpos.exception.menu;

public class MenuPriceOverThanProductsException extends RuntimeException {
    private static final String MESSAGE = "메뉴의 가격은 상품 가격의 총합보다 높을 수 없습니다.";

    public MenuPriceOverThanProductsException() {
        super(MESSAGE);
    }
}
