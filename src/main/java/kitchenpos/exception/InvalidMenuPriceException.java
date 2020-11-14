package kitchenpos.exception;

import java.math.BigDecimal;

public class InvalidMenuPriceException extends BusinessException {
    public InvalidMenuPriceException() {
        super("Cannot create Menu with null or negative Price");
    }
}
