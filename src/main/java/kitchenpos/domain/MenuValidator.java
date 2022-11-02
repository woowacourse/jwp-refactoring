package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuRequest;

public interface MenuValidator {

    void validatePriceByProducts(BigDecimal price, List<MenuProduct> menuProducts, MenuRequest menuRequest);
}
