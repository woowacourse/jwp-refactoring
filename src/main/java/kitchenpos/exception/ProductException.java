package kitchenpos.exception;

public class ProductException extends RuntimeException {

    private final ProductExceptionType exceptionType;

    public ProductException(ProductExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ProductExceptionType exceptionType() {
        return exceptionType;
    }
}
