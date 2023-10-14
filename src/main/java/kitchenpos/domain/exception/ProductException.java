package kitchenpos.domain.exception;

import kitchenpos.basic.BasicException;

public class ProductException extends BasicException {

    public ProductException(final ProductExceptionType exceptionType) {
        super(exceptionType);
    }
}
