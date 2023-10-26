package kitchenpos.menu.application;

import java.math.BigDecimal;

public interface ProductTotalPriceValidator {

    BigDecimal getTotalPriceThrowIfNotExist(final Long productId, final BigDecimal quantity);
}
