package kitchenpos.exception;

public class MenuPriceIsBiggerThanActualPriceException extends RuntimeException {

    private static final String MESSAGE = "메뉴 가격은 메뉴 상품 가격들의 합보다 클 수 없습니다.";

    public MenuPriceIsBiggerThanActualPriceException() {
        super(MESSAGE);
    }
}
