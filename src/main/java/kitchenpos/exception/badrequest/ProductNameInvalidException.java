package kitchenpos.exception.badrequest;

public class ProductNameInvalidException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "상품명이 유효하지 않습니다";
    private static final String MESSAGE_FORMAT = "상품명이 유효하지 않습니다 : %s";

    public ProductNameInvalidException() {
        super(DEFAULT_MESSAGE);
    }

    public ProductNameInvalidException(final String invalidName) {
        super(String.format(MESSAGE_FORMAT, invalidName));
    }
}
