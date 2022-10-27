package kitchenpos.exception;

public class MenuTotalPriceException extends IllegalArgumentException {

    public MenuTotalPriceException() {
        super("메뉴 가격이 상품의 가격합보다 큽니다");
    }
}
