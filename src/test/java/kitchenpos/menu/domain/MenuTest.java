package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.domain.model.MenuProduct;
import kitchenpos.menu.supports.MenuFixture;
import kitchenpos.menu.supports.MenuProductFixture;
import kitchenpos.product.domain.model.Product;
import kitchenpos.product.supports.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @Nested
    class 메뉴_상품을_등록한다 {

        @Test
        void 메뉴_가격이_상품_가격의_총_합보다_크면_예외() {
            // given
            int menuPrice = 13000;

            Product product1 = ProductFixture.fixture().id(1L).price(2000).build();
            Product product2 = ProductFixture.fixture().id(2L).price(3000).build();
            Menu menu = MenuFixture.fixture().price(menuPrice).build();
            List<MenuProduct> menuProducts = List.of(
                MenuProductFixture.fixture().menu(menu).product(product1).quantity(3L).build(),
                MenuProductFixture.fixture().product(product2).quantity(2L).build()
            );

            // when & then
            assertThatThrownBy(() -> menu.setUpMenuProducts(menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 상품의 가격 총 합보다 클 수 없습니다.");
        }

        @Test
        void 성공() {
            // given
            int menuPrice = 8000;

            Product product1 = ProductFixture.fixture().id(1L).price(2000).build();
            Product product2 = ProductFixture.fixture().id(2L).price(3000).build();
            Menu menu = MenuFixture.fixture().price(menuPrice).build();
            List<MenuProduct> menuProducts = List.of(
                MenuProductFixture.fixture().menu(menu).product(product1).quantity(3L).build(),
                MenuProductFixture.fixture().product(product2).quantity(2L).build()
            );

            // when & then
            assertThatNoException()
                .isThrownBy(() -> menu.setUpMenuProducts(menuProducts));
        }
    }
}
