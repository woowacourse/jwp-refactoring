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

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_메뉴가_입력되면 {

            final BigDecimal 천원 = new BigDecimal(1000);
            final Menu menu = new Menu("후라이드치킨", 천원, createMenuGroup(), createMenuProducts());

            @Test
            void 해당_메뉴를_반환한다() {
                final Menu actual = menuService.create(menu);

                assertThat(actual).isNotNull();
            }

            private Long createMenuGroup() {
                final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("한마리치킨"));
                return menuGroup.getId();
            }

            private List<MenuProduct> createMenuProducts() {
                final Product savedProduct = productService.create(new Product("후라이드", new BigDecimal(1000)));
                final MenuProduct menuProduct = new MenuProduct(1L, savedProduct.getId(), 1);
                return List.of(menuProduct);
            }
        }

        @Nested
        class 가격이_null인_메뉴가_입력되면 {

            private final Menu menu = new Menu("후라이드치킨", null, 1L, null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 가격이_음수인_메뉴가_입력되면 {

            final BigDecimal 음수 = new BigDecimal(-1000);
            final Menu menu = new Menu("후라이드치킨", 음수, 1L, null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 존재하지_않는_메뉴_그룹으로_메뉴가_입력되면 {

            final BigDecimal 천원 = new BigDecimal(1000);
            final Menu menu = new Menu("후라이드치킨", 천원, 2L, null);

            @Test
            void 해당_메뉴를_반환한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출되면 {

            private final BigDecimal 천원 = new BigDecimal(1000);
            final Menu menu = new Menu("후라이드치킨", 천원, createMenuGroup(), createMenuProducts());

            @Test
            void 모든_메뉴를_반환한다() {
                menuService.create(menu);
                final List<Menu> actual = menuService.list();

                assertThat(actual).hasSize(1);
            }
        }
    }

    private Long createMenuGroup() {
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("한마리치킨"));
        return menuGroup.getId();
    }

    private List<MenuProduct> createMenuProducts() {
        final Product savedProduct = productService.create(new Product("후라이드", new BigDecimal(1000)));
        final MenuProduct menuProduct = new MenuProduct(1L, savedProduct.getId(), 1);
        return List.of(menuProduct);
    }
}
