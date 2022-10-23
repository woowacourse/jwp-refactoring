package kitchenpos.application;

import static kitchenpos.application.ServiceTestFixture.MENU_PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {

    @Nested
    class CreateMenuTest {

        @Test
        void create_fail_when_price_is_null() {
            Menu menu = new Menu();
            menu.setMenuGroupId(1L);
            menu.setMenuProducts(MENU_PRODUCTS);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_price_is_less_than_0() {
            Menu menu = new Menu();
            menu.setMenuGroupId(1L);
            menu.setMenuProducts(MENU_PRODUCTS);
            menu.setPrice(BigDecimal.valueOf(-1));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_groupId_is_not_exist() {
            Menu menu = new Menu();
            menu.setMenuGroupId(100L);
            menu.setMenuProducts(MENU_PRODUCTS);
            menu.setPrice(BigDecimal.valueOf(-1));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_price_is_more_than_product_price_sum() {
            Menu menu = new Menu();
            menu.setMenuGroupId(2L);
            menu.setMenuProducts(MENU_PRODUCTS);
            menu.setPrice(BigDecimal.valueOf(33000));
            menu.setName("순삭치킨");

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_success() {
            Menu menu = new Menu();
            menu.setMenuGroupId(2L);
            menu.setMenuProducts(MENU_PRODUCTS);
            menu.setPrice(BigDecimal.valueOf(20000));
            menu.setName("순삭치킨");

            Menu savedMenu = menuService.create(menu);

            assertThat(savedMenu.getName()).isEqualTo("순삭치킨");
        }
    }

    @Nested
    class ListMenuTest {

        @Test
        void list_success() {
            assertThat(menuService.list()).hasSize(6);
        }
    }

}