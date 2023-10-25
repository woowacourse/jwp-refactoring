package kitchenpos.menu.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.test.fixture.MenuFixture;
import kitchenpos.test.fixture.MenuGroupFixture;
import kitchenpos.test.fixture.MenuProductFixture;
import kitchenpos.test.fixture.ProductFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuProductsTest {

    @Nested
    class 상품_총_가격이_주어진_가격보다_작은지_확인_시 {

        @Test
        void 상품_총_가격이_작다면_TRUE_반환한다() {
            //given
            MenuGroup menuGroup = MenuGroupFixture.메뉴_그룹("간식");
            Menu menu = MenuFixture.메뉴("탕후루", BigDecimal.valueOf(10000), menuGroup);
            Product product = ProductFixture.상품("탕후루", BigDecimal.valueOf(9000));
            MenuProduct menuProduct = MenuProductFixture.메뉴_상품(product, 1);
            MenuProducts menuProducts = new MenuProducts(List.of(menuProduct));

            //when
            boolean result = menuProducts.isPriceLessThan(menu.getPrice());

            //then
            assertThat(result).isTrue();
        }

        @Test
        void 상품_총_가격이_크다면_FALSE_반환한다() {
            //given
            MenuGroup menuGroup = MenuGroupFixture.메뉴_그룹("간식");
            Menu menu = MenuFixture.메뉴("탕후루", BigDecimal.valueOf(10000), menuGroup);
            Product product = ProductFixture.상품("탕후루", BigDecimal.valueOf(11000));
            MenuProduct menuProduct = MenuProductFixture.메뉴_상품(product, 1);
            MenuProducts menuProducts = new MenuProducts(List.of(menuProduct));

            //when
            boolean result = menuProducts.isPriceLessThan(menu.getPrice());

            //then
            assertThat(result).isFalse();
        }
    }
}
