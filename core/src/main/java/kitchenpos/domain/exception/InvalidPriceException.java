package kitchenpos.domain.exception;

public class InvalidPriceException extends IllegalArgumentException {

    public InvalidPriceException() {
        super("유효한 메뉴 가격이 아닙니다.");
    }
}
