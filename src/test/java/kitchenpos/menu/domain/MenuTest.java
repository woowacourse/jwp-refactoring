package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.fixtures.MenuFixtures;
import kitchenpos.fixtures.MenuGroupFixtures;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴를_생성한다() {
        List<MenuProduct> menuProducts = MenuFixtures.createMenuProducts();
        MenuGroup menuGroup = MenuGroupFixtures.createMenuGroup();
        Price price = menuProducts.stream()
            .map(menuProduct -> menuProduct.getProduct().getPrice())
            .reduce(Price::sum)
            .orElseGet(() -> Price.ZERO);

        Menu menu = new Menu("name", price, menuGroup.getId(), menuProducts);
        assertAll(
            () -> assertThat(menu.getName()).isEqualTo("name"),
            () -> assertThat(menu.getPrice()).isEqualTo(price),
            () -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
            () -> assertThat(menu.getMenuProducts()).hasSize(menuProducts.size())
        );
    }

    @Test
    void 생성_시_가격이_상품들의_가격합보다_크면_예외를_반환한다() {
        List<MenuProduct> menuProducts = MenuFixtures.createMenuProducts();
        Price invalidPrice = menuProducts.stream()
            .map(menuProduct -> menuProduct.getProduct().getPrice())
            .reduce(Price::sum)
            .orElseGet(() -> Price.ZERO)
            .sum(new Price(BigDecimal.ONE));

        assertThrows(IllegalStateException.class,
            () -> new Menu("name", invalidPrice, MenuGroupFixtures.createMenuGroup().getId(), menuProducts));
    }
}