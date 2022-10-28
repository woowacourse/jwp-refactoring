package kitchenpos.application;

import static kitchenpos.application.ServiceTestFixture.MENU_PRODUCT_REQUESTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.dto.MenuRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest {

    @Nested
    class CreateMenuTest extends ServiceTest {

        @Test
        void create_fail_when_price_is_null() {
            final MenuRequest menuRequest = new MenuRequest("맛있는치킨",
                    null,
                    1L,
                    MENU_PRODUCT_REQUESTS);

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_price_is_less_than_0() {
            final MenuRequest menuRequest = new MenuRequest("맛있는치킨",
                    BigDecimal.valueOf(-1),
                    1L,
                    MENU_PRODUCT_REQUESTS);

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_groupId_is_not_exist() {
            final MenuRequest menuRequest = new MenuRequest("맛있는치킨",
                    BigDecimal.valueOf(1),
                    100L,
                    MENU_PRODUCT_REQUESTS);

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_price_is_more_than_product_price_sum() {
            final MenuRequest menuRequest = new MenuRequest("맛있는치킨",
                    BigDecimal.valueOf(333000),
                    2L,
                    MENU_PRODUCT_REQUESTS);

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_success() {
            final MenuRequest menuRequest = new MenuRequest("순삭치킨",
                    BigDecimal.valueOf(20000),
                    1L,
                    MENU_PRODUCT_REQUESTS);

            final Menu savedMenu = menuService.create(menuRequest);

            assertThat(savedMenu.getName()).isEqualTo("순삭치킨");
        }
    }

    @Nested
    class ListMenuTest extends ServiceTest {

        @Test
        void list_success() {
            assertThat(menuService.list()).hasSize(6);
        }
    }
}
