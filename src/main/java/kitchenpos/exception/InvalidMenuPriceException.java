package kitchenpos.exception;

public class InvalidMenuPriceException extends RuntimeException {

    public InvalidMenuPriceException() {
        super("메뉴의 가격이 메뉴 제품들의 갯수에 따른 총액보다 비싸거나 메뉴의 가격이 null이거나 음수입니다.");
    }
}
