package kitchenpos.common.exception;

public class PriceEmptyException extends RuntimeException {

    public PriceEmptyException() {
        super("상품의 가격은 0원보다 커야합니다.");
    }
}
