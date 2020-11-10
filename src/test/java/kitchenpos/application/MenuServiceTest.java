package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenuByValidInput() {
        BigDecimal price = BigDecimal.valueOf(10000L);
        MenuGroup menuGroup = menuGroupService.create(createMenuGroup(null, "한마리 치킨"));
        Product product = productService.create(createProduct(null, "후라이드 치킨", price));
        Menu menuRequest = createMenu(null, "후라이드 치킨", price, menuGroup.getId(),
            Collections.singletonList(createMenuProduct(null, null, product.getId(), 1)));

        Menu menu = menuService.create(menuRequest);

        assertAll(
            () -> assertThat(menu.getId()).isNotNull(),
            () -> assertThat(menu.getName()).isEqualTo(menuRequest.getName()),
            () -> assertThat(menu.getPrice().longValue()).isEqualTo(price.longValue()),
            () -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
            () -> assertThat(menu.getMenuProducts()).size().isEqualTo(1)
        );
    }

    @DisplayName("메뉴 생성시 잘못된 가격을 입력한 경우 예외 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void createMenuByInvalidInputWithNegativePrice(String value) {
        BigDecimal price = BigDecimal.valueOf(10000L);
        MenuGroup menuGroup = menuGroupService.create(createMenuGroup(null, "한마리 치킨"));
        Product product = productService.create(createProduct(null, "후라이드 치킨", price));

        BigDecimal menuPrice = Objects.isNull(value) ? null : BigDecimal.valueOf(-1L);
        Menu menuRequest = createMenu(null, "후라이드 치킨", menuPrice, menuGroup.getId(),
            Collections.singletonList(createMenuProduct(null, null, product.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 그룹")
    @ParameterizedTest
    @NullAndEmptySource
    void createMenuByInvalidInputNotExistingMenuGroup(String value) {
        BigDecimal price = BigDecimal.valueOf(10000L);
        Product product = productService.create(createProduct(null, "후라이드 치킨", price));
        Long menuGroupId = Objects.isNull(value) ? null : 1L;
        Menu menuRequest = createMenu(null, "후라이드 치킨", price, menuGroupId,
            Collections.singletonList(createMenuProduct(null, null, product.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성시 상품 금액의 총합보다 메뉴의 가격이 높을시 예외 발생")
    @Test
    void createMenuByInvalidInputExceedingSumOfPrices() {
        BigDecimal price = BigDecimal.valueOf(10000L);

        MenuGroup menuGroup = menuGroupService.create(createMenuGroup(null, "한마리 치킨"));
        Product product = productService.create(createProduct(null, "후라이드 치킨", price));
        BigDecimal menuPrice = BigDecimal.valueOf(price.longValue() + 1);
        Menu menuRequest = createMenu(null, "후라이드 치킨", menuPrice, menuGroup.getId(),
            Collections.singletonList(createMenuProduct(null, null, product.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void findAll() {
        MenuGroup menuGroup = menuGroupService.create(createMenuGroup(null, "한마리 치킨"));
        Product friedChicken = productService.create(createProduct(null, "후라이드 치킨", BigDecimal.valueOf(10000L)));
        Product seasoningChicken = productService.create(createProduct(null, "양념 치킨", BigDecimal.valueOf(11000L)));
        Menu menuRequest1 = createMenu(null, friedChicken.getName(), friedChicken.getPrice(), menuGroup.getId(),
            Collections.singletonList(createMenuProduct(null, null, friedChicken.getId(), 1)));
        Menu menuRequest2 = createMenu(null, seasoningChicken.getName(), seasoningChicken.getPrice(), menuGroup.getId(),
            Collections.singletonList(createMenuProduct(null, null, seasoningChicken.getId(), 1)));

        menuService.create(menuRequest1);
        menuService.create(menuRequest2);
        List<Menu> menus = menuService.list();

        assertThat(menus).size().isEqualTo(2);
    }
}