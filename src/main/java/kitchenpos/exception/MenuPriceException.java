package kitchenpos.exception;

public class MenuPriceException extends BadRequestException {

    private static final String ERROR_MESSAGE = "메뉴 가격은 상품의 총 가격을 넘을 수 없습니다.";

    public MenuPriceException() {
        super(ERROR_MESSAGE);
    }
}
