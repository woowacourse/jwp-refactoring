package kitchenpos.domain.exception;

public class InvalidProductPriceException extends RuntimeException {

    public InvalidProductPriceException() {
        super("상품의 가격은 0원 이상이어야 합니다.");
    }
}
