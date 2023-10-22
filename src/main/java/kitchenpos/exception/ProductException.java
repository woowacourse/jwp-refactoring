package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public abstract class ProductException extends BaseException {

    public ProductException(final String message, final HttpStatus status) {
        super(message, status);
    }

    public static class NoPriceException extends ProductException {

        public NoPriceException() {
            super("가격은 필수입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NegativePriceException extends ProductException {
        public NegativePriceException(final Long price) {
            super("상품의 가격은" +price+"원일 수 없습니다 0원 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
