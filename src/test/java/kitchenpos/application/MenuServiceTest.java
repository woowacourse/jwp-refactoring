package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

class MenuServiceTest extends ServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "백마리치킨"));
        Product product = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menuRequest = createMenu(null, "양념치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
            Collections.singletonList(menuProduct));

        Menu saved = menuService.create(menuRequest);

        assertAll(
            () -> assertThat(saved.getId()).isNotNull(),
            () -> assertThat(saved.getName()).isEqualTo(menuRequest.getName()),
            () -> assertThat(saved.getPrice().longValue()).isEqualTo(
                menuRequest.getPrice().longValue()),
            () -> assertThat(saved.getMenuGroupId()).isEqualTo(menuRequest.getMenuGroupId()),
            () -> assertThat(saved.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴 등록 시, 메뉴그룹에 속하지 않은 메뉴는 등록할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = 1)
    @NullSource
    void create_NonExistingMenuGroup_ThrownException(Long menuGroupId) {
        Product product = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menuRequest = createMenu(null, "양념치킨", BigDecimal.valueOf(18_000), menuGroupId,
            Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시, 가격이 null 혹은 음수면 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = "-1000")
    @NullSource
    void create_WithInvalidPrice_ThrownException(BigDecimal price) {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "백마리치킨"));
        Product product = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menuRequest = createMenu(null, "양념치킨", price, menuGroup.getId(),
            Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시, 메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.")
    @Test
    void create_OverSumOfProductsPrice_ThrownException() {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "백마리치킨"));
        Product product = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menuRequest = createMenu(null, "양념치킨", BigDecimal.valueOf(18_001), menuGroup.getId(),
            Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시, 1개 이상의 등록된 상품으로 메뉴를 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = 1)
    @NullSource
    void create_NonExistingProductId_ThrownException(Long productId) {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "백마리치킨"));
        MenuProduct menuProduct = createMenuProduct(null, null, productId, 1);
        Menu menuRequest = createMenu(null, "양념치킨", BigDecimal.valueOf(18_001), menuGroup.getId(),
            Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "백마리치킨"));
        Product product = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menuRequest = createMenu(null, "양념치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
            Collections.singletonList(menuProduct));
        menuDao.save(menuRequest);

        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(1);
    }
}
