package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest {

    @Nested
    class 메뉴_생성 extends IntegrationTest {

        @Test
        void 요청을_할_수_있다() {
            // given
            MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            Menu menu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            menu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));

            // when
            Menu extract = menuService.create(menu);

            // then
            assertThat(extract).isNotNull();
        }

        @Test
        void 요청에서_메뉴_금액이_0원_미만인_경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            Menu menu = new Menu("짜장면", BigDecimal.valueOf(-1), menuGroup.getId());
            menu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_메뉴_금액이_NULL_일_경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            Menu menu = new Menu("짜장면", null, menuGroup.getId());
            menu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_메뉴를_추가할때_메뉴의_상품_번호가_존재하지_않는_번호일_경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            Menu menu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            Long notRegisterProductId = 100L;
            menu.addMenuProducts(List.of(new MenuProduct(1L, null, notRegisterProductId, 1)));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_추가한_메뉴가_메뉴_그룹에_속해있지_않은경우_예외가_발생한다() {
            // given
            Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(0)));
            Menu menu = new Menu("짜장면", BigDecimal.valueOf(1000), null);
            menu.addMenuProducts(List.of(new MenuProduct(1L, menu.getId(), product.getId(), 1)));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_추가한_메뉴가격의_합이_상품가격의_합보다_높을경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(0)));
            Menu menu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            menu.addMenuProducts(List.of(new MenuProduct(1L, menu.getId(), product.getId(), 1)));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    /**
     * 현재 프로덕션에 추가되야 하는 방어로직
     * - MenuProduct에 대한 product ID가 동일한지를 검증
     */
}