package kitchenpos.exception;

public class MenuPriceIsNegativeException extends RuntimeException {

    private static final String MESSAGE = "메뉴 가격이 음수일 수 없습니다.";

    public MenuPriceIsNegativeException() {
        super(MESSAGE);
    }
}
