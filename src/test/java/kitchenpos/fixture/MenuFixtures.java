package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixtures {
    public static Menu create(
            String name,
            int price,
            MenuGroup menuGroup,
            List<MenuProduct> menuProducts
    ) {
        return new Menu(null, name, new Price(BigDecimal.valueOf(price)), menuGroup.getId(), menuProducts);
    }
}
