package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.support.IntegrationServiceTest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends IntegrationServiceTest {

    private static final List<MenuProduct> MENU_PRODUCTS;

    static  {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L));
        MENU_PRODUCTS = menuProducts;
    }

    @Nested
    class create_메서드는 {

        @Nested
        class 가격에_null을_입력할_경우 {

            private final BigDecimal NULL_PRICE = null;

            private final Menu menu = new Menu("후라이드치킨", NULL_PRICE, 1L, new ArrayList<>());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수이어야 합니다.");
            }
        }

        @Nested
        class 가격에_음수를_입력될_경우 {

            private final BigDecimal MINUS_PRICE = BigDecimal.valueOf(-1);

            private final Menu menu = new Menu("후라이드치킨", MINUS_PRICE, 1L, Lists.emptyList());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수이어야 합니다.");
            }
        }

        @Nested
        class 없는_메뉴_그룹의_id가_입력될_경우 {

            private final long NOT_FOUND_MENU_GROUP_ID = -1L;

            private final Menu menu = new Menu(
                    "후라이드치킨",
                    BigDecimal.valueOf(10_000),
                    NOT_FOUND_MENU_GROUP_ID,
                    Lists.emptyList()
            );

            @Test
            void 예외가_발생한다() {

                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 메뉴 그룹의 id입니다.");
            }
        }

        @Nested
        class 메뉴상품의_상품id가_존재하지_않는_경우 {

            private static final long NOT_EXIST_MENU_PRODUCT_NUMBER = 0L;

            private final Menu menu = new Menu("후라이드치킨", BigDecimal.valueOf(16000), 1L, errorMenuProducts());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("메뉴상품의 상품ID가 존재하지 않습니다.");
            }

            private List<MenuProduct> errorMenuProducts() {
                final List<MenuProduct> menuProducts = new ArrayList<>();
                menuProducts.add(new MenuProduct(NOT_EXIST_MENU_PRODUCT_NUMBER, 1));
                return menuProducts;
            }
        }

        @Nested
        class 메뉴_가격이_상품가격과_메뉴상품양의_곱보다_큰_경우 {

            private final BigDecimal ERROR_MENU_PRICE = BigDecimal.valueOf(9_900_000);

            private final Menu menu = new Menu("후라이드치킨", ERROR_MENU_PRICE, 1L, MENU_PRODUCTS);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("메뉴의 가격은 상품들의 총 가격보다 클 수 없습니다.");
            }
        }

        @Nested
        class 정상적으로_메뉴를_생성가능한_경우 {

            private final Menu menu = new Menu("후라이드치킨", BigDecimal.valueOf(16_000), 1L, MENU_PRODUCTS);

            @Test
            void 저장된_메뉴가_반환된다() {
                Menu savedMenu = menuService.create(menu);

                assertAll(
                        () -> assertThat(savedMenu).isNotNull(),
                        () -> assertThat(menu.getId()).isNull(),
                        () -> assertThat(savedMenu.getId()).isNotNull()
                );
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Test
        void 메뉴목록을_반환한다() {
            List<Menu> actual = menuService.list();

            assertThat(actual).hasSize(6);
        }
    }
}