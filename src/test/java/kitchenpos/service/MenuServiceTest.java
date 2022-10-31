package kitchenpos.service;

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductsRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.util.FakeMenuDao;
import kitchenpos.util.FakeMenuGroupDao;
import kitchenpos.util.FakeMenuProductDao;
import kitchenpos.util.FakeProductDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuServiceTest {

    private final MenuDao menuDao = new FakeMenuDao();
    private final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
    private final MenuProductDao menuProductDao = new FakeMenuProductDao();
    private final ProductDao productDao = new FakeProductDao();
    private final MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        preprocessWhenCreate(new MenuGroup(3L, "메뉴그룹"),
                List.of(new Product(5L, "육회", 12000L)),
                List.of(new MenuProduct(5L, 2L)));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24000L, 3L,
                List.of(new MenuProductsRequest(5L, 2L)));

        MenuResponse menu = menuService.create(menuCreateRequest);

        assertAll(
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(3L),
                () -> assertThat(menu.getName()).isEqualTo("2인육회"),
                () -> assertThat(menu.getPrice()).isEqualTo(24000L),
                () -> assertThat(menu.getMenuProducts().size()).isEqualTo(1)
        );
    }
    @DisplayName("가격이 null인 메뉴를 생성할 수 없다")
    @Test
    void create_priceNull() {
        preprocessWhenCreate(new MenuGroup(3L, "메뉴그룹"),
                List.of(new Product(5L, "육회", 12000L)),
                List.of(new MenuProduct(5L, 2L)));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", null, 3L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("가격이 음수인 메뉴를 생성할 수 없다")
    @Test
    void create_priceNegative() {
        preprocessWhenCreate(new MenuGroup(3L, "메뉴그룹"),
                List.of(new Product(5L, "육회", 12000L)),
                List.of(new MenuProduct(5L, 2L)));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", -1L, 3L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("메뉴 그룹이 없는 메뉴를 생성할 수 없다")
    @Test
    void create_notExistMenuGroup() {
        preprocessWhenCreate(new MenuGroup(3L, "메뉴그룹"),
                List.of(new Product(5L, "육회", 12000L)),
                List.of(new MenuProduct(5L, 2L)));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24000L, 4L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("제품이 없는 메뉴를 생성할 수 없다")
    @Test
    void create_notExistProduct() {
        preprocessWhenCreate(new MenuGroup(3L, "메뉴그룹"),
                List.of(new Product(5L, "육회", 12000L)),
                List.of(new MenuProduct(5L, 2L)));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24000L, 3L, List.of(new MenuProductsRequest(10L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("메뉴 가격이 전체 제품의 가격보다 큰 메뉴를 생성할 수 없다")
    @Test
    void create_menuPriceOverTotalProductPrices() {
        preprocessWhenCreate(new MenuGroup(3L, "메뉴그룹"),
                List.of(new Product(5L, "육회", 12000L)),
                List.of(new MenuProduct(5L, 2L)));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "2인육회", 24001L, 3L, List.of(new MenuProductsRequest(5L, 2L)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void list() {
        preprocessWhenList(2);

        List<MenuResponse> menus = menuService.list();

        assertThat(menus.size()).isEqualTo(2);
    }

    private void preprocessWhenCreate(MenuGroup menuGroup, List<Product> products, List<MenuProduct> menuProducts) {
        menuGroupDao.save(menuGroup);
        products.forEach(productDao::save);
        menuProducts.forEach(menuProductDao::save);
    }

    private void preprocessWhenList(int count) {
        for (int i = 0; i < count; i++) {
            menuDao.save(new Menu("test", 1L, 1L));
        }
    }
}
