package kitchenpos.product.domain.exception;

import java.math.BigDecimal;
import kitchenpos.KitchenPosException;

public class NoPriceException extends KitchenPosException {

    private static final String NO_PRICE_MESSAGE = "금액은 0원 이상이어야 합니다. \n현재 금액: ";

    public NoPriceException(final BigDecimal price) {
        super(NO_PRICE_MESSAGE + price);
    }
}
