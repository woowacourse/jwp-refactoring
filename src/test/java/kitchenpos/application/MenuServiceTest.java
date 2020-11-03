package kitchenpos.application;

import static kitchenpos.db.TestDataFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

class MenuServiceTest extends TruncateDatabaseConfig {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    ProductDao productDao;

    private Menu menu;

    @BeforeEach
    void setUp() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);
        menuProduct.setSeq(1L);

        menu = new Menu();
        menu.setName("메뉴1");
        menu.setPrice(BigDecimal.valueOf(10_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(menuProduct));
    }

    @DisplayName("메뉴 생성 실패 - 가격 null")
    @ParameterizedTest
    @NullSource
    void createFail_When_Price_Null(BigDecimal price) {
        menu.setPrice(price);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 가격 음수")
    @Test
    void createFail_When_Negative_Price() {
        menu.setPrice(BigDecimal.valueOf(-10_000));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 존재하지 않는 MenuGroupId")
    @Test
    void createFail_When_MenuGroupId_NotExist() {
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 가격이 Products 가격 합보다 클 경우")
    @Test
    void createFail_When_Price_Over_Sum_Of_ProductsPrice() {
        Product product = createProduct(null, "음료", BigDecimal.valueOf(3_000L));
        MenuProduct menuProduct = createMenuProduct(null, 1L, 1L);
        MenuGroup menuGroup = createMenuGroup(null, "음료류");

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        Menu menu = createMenu(1L, "음료 세트", BigDecimal.valueOf(3_200L), savedMenuGroup.getId(),
            Arrays.asList(menuProduct));

        productDao.save(product);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹 조회")
    @Test
    void list() {
        Product product = createProduct(null, "콜라", BigDecimal.valueOf(2_000L));
        Product savedProduct = productDao.save(product);

        MenuGroup menuGroup = createMenuGroup(null, "음료류");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        MenuProduct menuProduct = createMenuProduct(null, savedProduct.getId(), 1L);
        Menu menu = createMenu(null, "음료 세트", BigDecimal.valueOf(1_800L), savedMenuGroup.getId(),
            Arrays.asList(menuProduct));

        menuService.create(menu);
        List<Menu> menus = menuService.list();

        assertAll(
            () -> assertThat(menus.size()).isEqualTo(1)
        );
    }
}
