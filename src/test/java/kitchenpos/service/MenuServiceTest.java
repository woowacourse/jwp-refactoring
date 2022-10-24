package kitchenpos.service;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductsRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql({"classpath:/truncate.sql", "classpath:/set_up.sql"})
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    void create() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24000L, 3L,
                List.of(new MenuProductsRequest(5L, 2L)));

        Menu menu = menuService.create(menuCreateRequest);

        assertAll(
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(3L),
                () -> assertThat(menu.getName()).isEqualTo("2인육회"),
                () -> assertThat(menu.getPrice()).isEqualTo(24000L),
                () -> assertThat(menu.getMenuProducts()).isEqualTo(
                        List.of(new MenuProduct(menu.getId(), 5L, 2L)))
        );
    }

    @Test
    void create_priceNull() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", null, 3L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_priceNegative() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", -1L, 3L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_notExistMenuGroup() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24000L, 4L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_notExistProduct() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24000L, 3L, List.of(new MenuProductsRequest(10L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_menuPriceOverTotalProductPrices() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24001L, 3L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        List<Menu> menus = menuService.list();

        assertThat(menus.size()).isEqualTo(2);
    }
}
