package kitchenpos.exception;

import java.math.BigDecimal;

public class InvalidProductPriceException extends BusinessException {
    public InvalidProductPriceException(BigDecimal price) {
    }
}
