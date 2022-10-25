package kitchenpos.application;

import static java.util.Arrays.asList;
import static kitchenpos.support.MenuFixture.createMenu;
import static kitchenpos.support.MenuFixture.createMenu;
import static kitchenpos.support.MenuFixture.createMenuWithProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Lists.emptyList;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.support.IntegrationServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends IntegrationServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 가격에_null을_입력할_경우 {

            private final BigDecimal NULL_PRICE = null;

            private final Menu menu = new Menu("후라이드치킨", NULL_PRICE, 1L, emptyList());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수이어야 합니다.");
            }
        }

        @Nested
        class 가격에_음수를_입력될_경우 {

            private final int MINUS_PRICE = -1;

            private final Menu menu = createMenu(MINUS_PRICE);


            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수이어야 합니다.");
            }
        }

        @Nested
        class 없는_메뉴_그룹의_id가_입력될_경우 {

            private final long NOT_EXIST_MENU_GROUP_ID = -1L;

            private final Menu menu = createMenu(10_000, NOT_EXIST_MENU_GROUP_ID);


            @Test
            void 예외가_발생한다() {

                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 메뉴 그룹의 id입니다.");
            }
        }

        @Nested
        class 메뉴상품의_상품ID가_존재하지_않는_경우 {

            private static final long NOT_EXIST_MENU_PRODUCT_NUMBER = -1L;

            private final List<MenuProduct> ERROR_MENU_PRODUCTS = asList(new MenuProduct(NOT_EXIST_MENU_PRODUCT_NUMBER, 1));

            private final Menu menu = createMenu(16_000, ERROR_MENU_PRODUCTS);


            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("메뉴상품의 상품ID가 존재하지 않습니다.");
            }
        }

        @Nested
        class 메뉴_가격이_상품가격과_메뉴상품양의_곱보다_큰_경우 {

            private final int ERROR_MENU_PRICE = 9_900_000;

            private final Menu menu = createMenu(ERROR_MENU_PRICE);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("메뉴의 가격은 상품들의 총 가격보다 클 수 없습니다.");
            }
        }

        @Nested
        class 정상적으로_메뉴를_생성가능한_경우 {

            private final Menu menu = createMenuWithProduct(16_000, 1L);

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