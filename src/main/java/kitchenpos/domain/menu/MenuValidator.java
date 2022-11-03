package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.menu.MenuProduct;

public interface MenuValidator {

    void validatePriceByProducts(BigDecimal price, List<MenuProduct> menuProducts, MenuRequest menuRequest);
}
