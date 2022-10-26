package kitchenpos.support.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu createPepperoniMenu(Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu("페퍼로니", BigDecimal.valueOf(1000L), menuGroupId, menuProducts);
    }
}
