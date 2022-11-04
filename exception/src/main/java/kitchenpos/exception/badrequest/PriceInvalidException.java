package kitchenpos.exception.badrequest;

import java.math.BigDecimal;

public class PriceInvalidException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "가격이 유효하지 않습니다";
    private static final String MESSAGE_FORMAT = "가격이 유효하지 않습니다 : %s";

    public PriceInvalidException() {
        super(DEFAULT_MESSAGE);
    }

    public PriceInvalidException(final BigDecimal invalidPrice) {
        super(String.format(MESSAGE_FORMAT, invalidPrice));
    }
}


