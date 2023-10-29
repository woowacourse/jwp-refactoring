package kitchenpos.application.integration;


import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.CreateMenuRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroups.dto.CreateMenuGroupRequest;
import kitchenpos.menugroups.dto.MenuGroupResponse;
import kitchenpos.menugroups.exception.MenuGroupNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import kitchenpos.product.dto.CreateProductRequest;
import kitchenpos.product.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends ApplicationIntegrationTest {
    private MenuGroupResponse menuGroup;

    private MenuProductRequest menuProduct1;
    private MenuProductRequest menuProduct2;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupService.create(CreateMenuGroupRequest.of("치킨"));
        final ProductResponse product1 = productService.create(CreateProductRequest.of("후라이드", BigDecimal.valueOf(16000).longValue()));
        final ProductResponse product2 = productService.create(CreateProductRequest.of("양념치킨", BigDecimal.valueOf(16000).longValue()));
        menuProduct1 = MenuProductRequest.of(product1.getId(), 1);
        menuProduct2 = MenuProductRequest.of(product2.getId(), 1);
    }

    @Test
    void create_menu() {
        //given
        final List<MenuProductRequest> menuProducts = List.of(menuProduct1, menuProduct2);
        final Long price = 16000L;

        final CreateMenuRequest createMenuRequest = CreateMenuRequest.of("후라이드", price, menuGroup.getId(), menuProducts);

        //when
        final MenuResponse createdMenu = menuService.create(createMenuRequest);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(createdMenu.getId()).isNotNull();
            softAssertions.assertThat(createdMenu.getName()).isEqualTo(createMenuRequest.getName());
            softAssertions.assertThat(createdMenu.getPrice()).isEqualTo(createMenuRequest.getPrice());
            softAssertions.assertThat(createdMenu.getMenuGroupId()).isEqualTo(createMenuRequest.getMenuGroupId());
            softAssertions.assertThat(createdMenu.getMenuProducts().getItems()).hasSize(2);
        });

    }

    @Test
    void cannot_create_menu_with_empty_name() {
        //given
        final List<MenuProductRequest> menuProducts = List.of(menuProduct1, menuProduct2);
        final Long price = 16000L;

        final CreateMenuRequest createMenuRequest = CreateMenuRequest.of(null, price, menuGroup.getId(), menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_menu_with_negative_price() {
        //given
        final List<MenuProductRequest> menuProducts = List.of(menuProduct1, menuProduct2);
        final Long price = -16000L;

        final CreateMenuRequest createMenuRequest = CreateMenuRequest.of("후라이드", price, menuGroup.getId(), menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void cannot_create_menu_with_invalid_menu_group_id() {
        //given
        final List<MenuProductRequest> menuProducts = List.of(menuProduct1, menuProduct2);
        final Long price = 16000L;

        final CreateMenuRequest createMenuRequest = CreateMenuRequest.of("후라이드", price, 100L, menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest))
                .isInstanceOf(MenuGroupNotFoundException.class);

    }

    @Test
    void cannot_create_menu_with_empty_menu_products() {
        //given
        final List<MenuProductRequest> menuProducts = List.of();
        final Long price = 16000L;

        final CreateMenuRequest createMenuRequest = CreateMenuRequest.of("후라이드", price, menuGroup.getId(), menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Menu.MINIMUM_MENU_PRODUCTS_SIZE_ERROR_MESSAGE);
    }

    @Test
    void throw_when_menu_price_over_sum_of_menu_products_price() {
        //given
        final List<MenuProductRequest> menuProducts = List.of(menuProduct1, menuProduct2);
        final Long price = 100000L;

        final CreateMenuRequest createMenuRequest = CreateMenuRequest.of("후라이드", price, menuGroup.getId(), menuProducts);

        //when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Menu.MENU_PRICE_IS_BIGGER_THAN_SUM_ERROR_MESSAGE);
    }

    @Test
    void list_menus() {

    }
}