package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.IntegrationTest;
import kitchenpos.application.dto.MenuCreationRequest;
import kitchenpos.application.dto.MenuProductWithQuantityRequest;
import kitchenpos.application.dto.result.MenuResult;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @Test
    void create_menu_success() {
        // given
        final MenuGroup menuGroup = generateMenuGroup("chicken-group");
        final Product chicken = generateProduct("chicken", 20000L);
        final Product cheeseBall = generateProduct("cheese-ball", 5000L);
        final List<MenuProductWithQuantityRequest> productsRequest = List.of(
                new MenuProductWithQuantityRequest(chicken.getId(), 1L),
                new MenuProductWithQuantityRequest(cheeseBall.getId(), 2L)
        );
        final MenuCreationRequest creationRequest = new MenuCreationRequest(
                "chicken-set",
                BigDecimal.valueOf(28000),
                menuGroup.getId(),
                productsRequest
        );

        // when
        final MenuResult createdMenu = menuService.create(creationRequest);

        // then
        assertThat(createdMenu.getId()).isNotNull();
        assertThat(createdMenu.getMenuProductResults()).hasSize(2);
    }

    @Nested
    class create_menu_failure {

        @Test
        void price_is_zero() {
            // given
            final MenuGroup menuGroup = generateMenuGroup("chicken-group");
            final MenuCreationRequest requestWithZeroPrice = new MenuCreationRequest(
                    "chicken-set",
                    BigDecimal.valueOf(0L),
                    menuGroup.getId(),
                    null
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(requestWithZeroPrice))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Price must be greater than zero.");
        }

        @Test
        void without_price() {
            // given
            final MenuGroup menuGroup = generateMenuGroup("chicken-group");
            final MenuCreationRequest requestWithoutPrice = new MenuCreationRequest(
                    "chicken-set",
                    null,
                    menuGroup.getId(),
                    null
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(requestWithoutPrice))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Price must not be null.");
        }

        @Test
        void menu_group_is_not_exist() {
            // given
            final Long notExistId = 10000L;
            final MenuCreationRequest requestWithAbsenceMenuGroup = new MenuCreationRequest(
                    "chicken-set",
                    BigDecimal.valueOf(28000L),
                    notExistId,
                    null
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(requestWithAbsenceMenuGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("MenuGroup does not exist.");
        }

        @Test
        void menu_product_is_not_exist() {
            // given
            final MenuGroup menuGroup = generateMenuGroup("chicken-group");
            final Long notExistId = 10000L;
            final MenuCreationRequest requestWithAbsenceProduct = new MenuCreationRequest(
                    "chicken-set",
                    BigDecimal.valueOf(28000L),
                    menuGroup.getId(),
                    List.of(new MenuProductWithQuantityRequest(notExistId, 3L))
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(requestWithAbsenceProduct))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Product does not exist.");
        }

        @Test
        void menu_price_is_over_than_total_sum_of_menu_products() {
            // given
            final MenuGroup menuGroup = generateMenuGroup("chicken-group");
            final Product chicken = generateProduct("chicken", 20000L);
            final Product cheeseBall = generateProduct("cheese-ball", 5000L);
            final List<MenuProductWithQuantityRequest> menuProductRequest = List.of(
                    new MenuProductWithQuantityRequest(chicken.getId(), 1L),
                    new MenuProductWithQuantityRequest(cheeseBall.getId(), 2L)
            );
            final MenuCreationRequest requestWithOverProductsPrice = new MenuCreationRequest(
                    "chicken-set",
                    BigDecimal.valueOf(30000L + 1L),
                    menuGroup.getId(),
                    menuProductRequest
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(requestWithOverProductsPrice))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Sum of menu products price must be greater than menu price.");
        }
    }

    @Test
    void list() {
        // given
        final MenuGroup menuGroup = generateMenuGroup("chicken-group");
        final Product chicken = generateProduct("chicken", 20000L);
        final Product cheeseBall = generateProduct("cheese-ball", 5000L);
        final Menu menuA = new Menu("chicken-set-A", BigDecimal.valueOf(28000L), menuGroup);
        final List<MenuProduct> menuProductsA = List.of(
                new MenuProduct(menuA, chicken.getId(), 1L),
                new MenuProduct(menuA, cheeseBall.getId(), 2L)
        );

        final Menu menuB = new Menu("chicken-set-A", BigDecimal.valueOf(24000L), menuGroup);
        final List<MenuProduct> menuProductsB = List.of(
                new MenuProduct(menuB, chicken.getId(), 1L),
                new MenuProduct(menuB, cheeseBall.getId(), 1L)
        );
        menuA.applyMenuProducts(menuProductsA, menuA.getPrice());
        menuB.applyMenuProducts(menuProductsB, menuB.getPrice());
        menuRepository.save(menuA);
        menuRepository.save(menuB);

        // when
        final List<MenuResult> list = menuService.list();

        // then
        assertThat(list).hasSize(2);
    }
}
