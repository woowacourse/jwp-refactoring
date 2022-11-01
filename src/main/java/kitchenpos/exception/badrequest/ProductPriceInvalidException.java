package kitchenpos.exception.badrequest;

import java.math.BigDecimal;

public class ProductPriceInvalidException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "상품 가격이 유효하지 않습니다";
    private static final String MESSAGE_FORMAT = "상품 가격이 유효하지 않습니다 : %s";

    public ProductPriceInvalidException() {
        super(DEFAULT_MESSAGE);
    }

    public ProductPriceInvalidException(final BigDecimal invalidPrice) {
        super(String.format(MESSAGE_FORMAT, invalidPrice));
    }
}


