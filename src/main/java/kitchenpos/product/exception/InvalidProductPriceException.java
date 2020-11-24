package kitchenpos.product.exception;

import kitchenpos.common.CreateFailException;

public class InvalidProductPriceException extends CreateFailException {
    public InvalidProductPriceException() {
        super("유효하지 않은 Product 금액 입력입니다.");
    }
}
