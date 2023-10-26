package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.MenuCreationRequest;
import kitchenpos.menu.application.dto.MenuProductWithQuantityRequest;
import kitchenpos.menu.application.dto.MenuResult;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuValidator menuValidator;

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
                    List.of()
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
                    List.of()
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
        final List<MenuProduct> menuProductsA = List.of(
                new MenuProduct(chicken.getId(), 1L),
                new MenuProduct(cheeseBall.getId(), 2L)
        );
        menuRepository.save(new Menu(
                "chicken-set-A",
                BigDecimal.valueOf(28000L),
                menuGroup,
                menuProductsA,
                menuValidator
        ));

        final List<MenuProduct> menuProductsB = List.of(
                new MenuProduct(chicken.getId(), 1L),
                new MenuProduct(cheeseBall.getId(), 1L)
        );
        menuRepository.save(new Menu(
                "chicken-set-A",
                BigDecimal.valueOf(24000L),
                menuGroup,
                menuProductsB,
                menuValidator
        ));
        menuProductRepository.saveAll(menuProductsA);
        menuProductRepository.saveAll(menuProductsB);

        // when
        final List<MenuResult> list = menuService.list();

        // then
        assertThat(list).hasSize(2);
    }
}
