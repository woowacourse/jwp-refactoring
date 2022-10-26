package kitchenpos.service;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductsRequest;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("메뉴를 생성한다")
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
    @DisplayName("가격이 null인 메뉴를 생성할 수 없다")
    @Test
    void create_priceNull() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", null, 3L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("가격이 음수인 메뉴를 생성할 수 없다")
    @Test
    void create_priceNegative() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", -1L, 3L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("메뉴 그룹이 없는 메뉴를 생성할 수 없다")
    @Test
    void create_notExistMenuGroup() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24000L, 4L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("제품이 없는 메뉴를 생성할 수 없다")
    @Test
    void create_notExistProduct() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24000L, 3L, List.of(new MenuProductsRequest(10L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("메뉴 가격이 전체 제품의 가격보다 큰 메뉴를 생성할 수 없다")
    @Test
    void create_menuPriceOverTotalProductPrices() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24001L, 3L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void list() {
        List<Menu> menus = menuService.list();

        assertThat(menus.size()).isEqualTo(2);
    }
}
