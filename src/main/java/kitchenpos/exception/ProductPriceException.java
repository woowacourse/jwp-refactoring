package kitchenpos.exception;

public class ProductPriceException extends BadRequestException {

    private static final String ERROR_MESSAGE = "상품 가격이 유효하지 않습니다.";

    public ProductPriceException() {
        super(ERROR_MESSAGE);
    }
}
