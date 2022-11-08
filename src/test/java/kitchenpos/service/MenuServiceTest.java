package kitchenpos.service;

import kitchenpos.application.MenuService;
import kitchenpos.dao.*;
import kitchenpos.domain.history.MenuHistory;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.*;
import kitchenpos.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuServiceTest {

    private final MenuDao menuDao = new FakeMenuDao();
    private final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
    private final MenuProductDao menuProductDao = new FakeMenuProductDao();
    private final ProductDao productDao = new FakeProductDao();
    private final MenuHistoryDao menuHistoryDao = new FakeMenuHistoryDao();
    private final MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao, menuHistoryDao);
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

    @DisplayName("메뉴의 가격을 바꾸는 경우 변경 목록이 저장되어야 한다")
    @Test
    void changePrice() {
        preprocessWhenCreate(new MenuGroup(1L, "test"),
                List.of(
                        new Product(1L, "광어초밥", 1000L),
                        new Product(2L, "연어초밥", 2000L)),
                List.of(
                        new MenuProduct(1L, 2L),
                        new MenuProduct(2L, 3L)
                ));
        MenuCreateRequest request = new MenuCreateRequest("초밥세트", 8000L, 1L, List.of(
                new MenuProductsRequest(1L, 2L),
                new MenuProductsRequest(2L, 3L)));
        MenuResponse menuResponse = menuService.create(request);
        MenuPriceChangeRequest menuPriceChangeRequest = new MenuPriceChangeRequest(menuResponse.getId(), 7000L, LocalDateTime.now());

        menuService.changMenuPrice(menuPriceChangeRequest);
        List<MenuHistory> result = menuHistoryDao.findAllByDateAndMenuId(menuPriceChangeRequest.getMenuId(), LocalDateTime.now());

        assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("존재하지 않는 메뉴의 가격을 바꾸는 경우 변경 목록이 저장되지 않는다")
    @Test
    void changePrice_notExistMenu() {
        preprocessWhenCreate(new MenuGroup(1L, "test"),
                List.of(
                        new Product(1L, "광어초밥", 1000L),
                        new Product(2L, "연어초밥", 2000L)),
                List.of(
                        new MenuProduct(1L, 2L),
                        new MenuProduct(2L, 3L)
                ));
        MenuPriceChangeRequest menuPriceChangeRequest = new MenuPriceChangeRequest(100L, 7000L, LocalDateTime.now());

        assertThatThrownBy(() -> menuService.changMenuPrice(menuPriceChangeRequest));
    }

    @DisplayName("메뉴의 가격을 음수로 바꾸는 경우 변경 목록이 저장되지 않는다")
    @Test
    void changePrice_negativePrice() {
        preprocessWhenCreate(new MenuGroup(1L, "test"),
                List.of(
                        new Product(1L, "광어초밥", 1000L),
                        new Product(2L, "연어초밥", 2000L)),
                List.of(
                        new MenuProduct(1L, 2L),
                        new MenuProduct(2L, 3L)
                ));

        MenuCreateRequest request = new MenuCreateRequest("초밥세트", 8000L, 1L, List.of(
                new MenuProductsRequest(1L, 2L),
                new MenuProductsRequest(2L, 3L)));
        MenuResponse menuResponse = menuService.create(request);
        MenuPriceChangeRequest menuPriceChangeRequest = new MenuPriceChangeRequest(menuResponse.getId(), -1L, LocalDateTime.now());


        assertThatThrownBy(() -> menuService.changMenuPrice(menuPriceChangeRequest));
    }

    @DisplayName("메뉴의 가격을 null로 바꾸는 경우 변경 목록이 저장되지 않는다")
    @Test
    void changePrice_nullPrice() {
        preprocessWhenCreate(new MenuGroup(1L, "test"),
                List.of(
                        new Product(1L, "광어초밥", 1000L),
                        new Product(2L, "연어초밥", 2000L)),
                List.of(
                        new MenuProduct(1L, 2L),
                        new MenuProduct(2L, 3L)
                ));

        MenuCreateRequest request = new MenuCreateRequest("초밥세트", 8000L, 1L, List.of(
                new MenuProductsRequest(1L, 2L),
                new MenuProductsRequest(2L, 3L)));
        MenuResponse menuResponse = menuService.create(request);
        MenuPriceChangeRequest menuPriceChangeRequest = new MenuPriceChangeRequest(menuResponse.getId(), null, LocalDateTime.now());


        assertThatThrownBy(() -> menuService.changMenuPrice(menuPriceChangeRequest));
    }

    @DisplayName("메뉴의 이름을 바꾸는 경우 변경 목록이 저장되어야 한다")
    @Test
    void changeName() {
        preprocessWhenCreate(new MenuGroup(1L, "test"),
                List.of(
                        new Product(1L, "광어초밥", 1000L),
                        new Product(2L, "연어초밥", 2000L)),
                List.of(
                        new MenuProduct(1L, 2L),
                        new MenuProduct(2L, 3L)
                ));
        MenuCreateRequest request = new MenuCreateRequest("초밥세트", 8000L, 1L, List.of(
                new MenuProductsRequest(1L, 2L),
                new MenuProductsRequest(2L, 3L)));
        MenuResponse menuResponse = menuService.create(request);
        MenuNameChangeRequest menuNameChangeRequest = new MenuNameChangeRequest(menuResponse.getId(), "밥초세트", LocalDateTime.now());

        menuService.changMenuName(menuNameChangeRequest);
        List<MenuHistory> result = menuHistoryDao.findAllByDateAndMenuId(menuNameChangeRequest.getMenuId(), LocalDateTime.now());

        assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("존재하지 않는 메뉴의 이름을 바꾸는 경우 변경 목록이 저장되지 않는다")
    @Test
    void changeName_notExistMenu() {
        preprocessWhenCreate(new MenuGroup(1L, "test"),
                List.of(
                        new Product(1L, "광어초밥", 1000L),
                        new Product(2L, "연어초밥", 2000L)),
                List.of(
                        new MenuProduct(1L, 2L),
                        new MenuProduct(2L, 3L)
                ));
        MenuNameChangeRequest menuNameChangeRequest = new MenuNameChangeRequest(100L, "밥초세트", LocalDateTime.now());

        assertThatThrownBy(() -> menuService.changMenuName(menuNameChangeRequest));
    }

    @DisplayName("메뉴의 이름을 null로 바꾸는 경우 변경 목록이 저장되지 않는다")
    @Test
    void changeName_nullName() {
        preprocessWhenCreate(new MenuGroup(1L, "test"),
                List.of(
                        new Product(1L, "광어초밥", 1000L),
                        new Product(2L, "연어초밥", 2000L)),
                List.of(
                        new MenuProduct(1L, 2L),
                        new MenuProduct(2L, 3L)
                ));
        MenuCreateRequest request = new MenuCreateRequest("초밥세트", 8000L, 1L, List.of(
                new MenuProductsRequest(1L, 2L),
                new MenuProductsRequest(2L, 3L)));
        MenuResponse menuResponse = menuService.create(request);
        MenuNameChangeRequest menuNameChangeRequest = new MenuNameChangeRequest(menuResponse.getId(), null, LocalDateTime.now());

        assertThatThrownBy(() -> menuService.changMenuName(menuNameChangeRequest));
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
