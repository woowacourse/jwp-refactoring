package kitchenpos.menu.exception;

public class InvalidMenuProductsPriceException extends MenuProductExcpetion {
    private final static String error = "메뉴의 가격은 메뉴 상품 가격 합 이하여야 합니다.";
    public InvalidMenuProductsPriceException() {
        super(error);
    }
}
