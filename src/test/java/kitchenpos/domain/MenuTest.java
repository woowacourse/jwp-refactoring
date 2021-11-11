package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.fixtures.MenuFixtures;
import kitchenpos.fixtures.MenuGroupFixtures;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 생성_시_가격이_상품들의_가격합보다_크면_예외를_반환한다() {
        List<MenuProduct> menuProducts = MenuFixtures.createMenuProducts();
        Price invalidPrice = menuProducts.stream()
            .map(menuProduct -> menuProduct.getProduct().getPrice())
            .reduce(Price::sum)
            .orElseGet(() -> Price.ZERO)
            .sum(new Price(BigDecimal.ONE));

        assertThrows(IllegalStateException.class,
            () -> new Menu("name", invalidPrice, MenuGroupFixtures.createMenuGroup(), menuProducts));
    }
}