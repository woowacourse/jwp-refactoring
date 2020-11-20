package kitchenpos.exception;

import java.math.BigDecimal;
import java.util.Objects;

public class InvalidProductPriceException extends BusinessException {
    public InvalidProductPriceException() {
        super("NULL is invalid price");
    }

    public InvalidProductPriceException(BigDecimal price) {
        super(String.format("%d is invalid price", price.longValue()));
    }
}
