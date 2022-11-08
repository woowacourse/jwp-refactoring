package kitchenpos.exception.badrequest;

public class ProductNameDuplicateException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "상품명은 중복될 수 없습니다";
    private static final String MESSAGE_FORMAT = "상품명은 중복될 수 없습니다 : %s";

    public ProductNameDuplicateException() {
        super(DEFAULT_MESSAGE);
    }

    public ProductNameDuplicateException(final String duplicateName) {
        super(String.format(MESSAGE_FORMAT, duplicateName));
    }
}
