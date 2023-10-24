package kitchenpos.exception;

public class ProductException extends BaseException {

    private final ProductExceptionType exceptionType;

    public ProductException(ProductExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public ProductExceptionType exceptionType() {
        return exceptionType;
    }
}
