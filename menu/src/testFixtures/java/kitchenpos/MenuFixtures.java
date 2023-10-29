package kitchenpos;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;

import java.math.BigDecimal;

import static java.util.Collections.singletonList;

public class MenuFixtures {

    private MenuFixtures() {
    }

    public static Menu of(final Long productId, final Long menuGroupId) {
        final MenuProduct menuProduct = new MenuProduct(productId, 4);

        return new Menu("떡볶이 세트",
                new Price(BigDecimal.valueOf(16000)),
                menuGroupId,
                singletonList(menuProduct));
    }
}
