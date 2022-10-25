package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

class MenuServiceTest extends ServiceTest {

    @Autowired
    protected MenuDao menuDao;
    @Autowired
    protected MenuGroupDao menuGroupDao;
    @Autowired
    protected MenuProductDao menuProductDao;
    @Autowired
    protected ProductDao productDao;
    @Autowired
    protected MenuService menuService;

    @Test
    @DisplayName("메뉴를 저장한다")
    void create() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Product createdProduct = createProduct();
        MenuProduct menuProduct = createMenuProduct(createdProduct);

        Menu menu = new Menu();
        menu.setName("test");
        menu.setPrice(BigDecimal.ONE);
        menu.setMenuGroupId(createdMenuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        // when
        Menu createdMenu = menuService.create(menu);

        // then
        assertAll(
            () -> assertThat(createdMenu).isNotNull(),
            () -> assertThat(createdMenu.getName()).isEqualTo("test")
        );
    }

    @Test
    @DisplayName("메뉴 가격은 함께 등록되어야 한다")
    void nullPrice() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Product createdProduct = createProduct();
        MenuProduct menuProduct = createMenuProduct(createdProduct);

        Menu menu = new Menu();
        menu.setName("test");
        menu.setMenuGroupId(createdMenuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격은 음수일 수 없다")
    void minusPrice() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Product createdProduct = createProduct();
        MenuProduct menuProduct = createMenuProduct(createdProduct);

        Menu menu = new Menu();
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(-100));
        menu.setMenuGroupId(createdMenuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹에 속해있지 않을 수 없다")
    void withoutMenuGroup() {
        // given
        Product createdProduct = createProduct();
        MenuProduct menuProduct = createMenuProduct(createdProduct);

        Menu menu = new Menu();
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(-100));
        menu.setMenuProducts(List.of(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("구성 상품 가격의 합을 초과하는 가격을 설정할 수 없다")
    void priceBiggerThanSum() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Product createdProduct = createProduct();
        MenuProduct menuProduct = createMenuProduct(createdProduct);

        Menu menu = new Menu();
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(101));
        menu.setMenuGroupId(createdMenuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록 조회한다")
    void list() {
        // given

        // when
        List<Menu> menus = menuService.list();

        // then
        assertAll(
            () -> assertThat(menus).hasSameSizeAs(menuDao.findAll())
        );
    }

    private MenuGroup createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("testGroup");
        return menuGroupDao.save(menuGroup);
    }

    private Product createProduct() {
        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        product.setName("testProduct");
        return productDao.save(product);
    }

    private MenuProduct createMenuProduct(Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(10L);
        return menuProduct;
    }
}
