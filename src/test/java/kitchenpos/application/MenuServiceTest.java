package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuProductDao menuProductDao;
    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() {
        // given
        Menu menu = new Menu();
        menu.setName("뿌링클치킨");
        menu.setPrice(BigDecimal.valueOf(18000));
        menu.setMenuGroupId(2L);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(10);
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);
        // when
        Menu createdMenu = menuService.create(menu);

        // then
        assertThat(createdMenu.getId()).isNotNull();
    }

    @Test
    @DisplayName("메뉴를 생성 시 가격이 null이라면 예외를 반환한다.")
    void create_WhenNullPrice() {
        // given
        Menu menu = new Menu();
        menu.setName("뿌링클치킨");
        menu.setMenuGroupId(2L);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(10);
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 생성 시 가격이 0보다 작다면 예외를 반환한다.")
    void create_WhenPriceUnderZero() {
        // given
        Menu menu = new Menu();
        menu.setName("뿌링클치킨");
        menu.setPrice(BigDecimal.valueOf(-1));
        menu.setMenuGroupId(2L);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(10);
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 존재하지 않는 MenuGroup이라면 예외를 반환한다.")
    void create_WhenNotExistMenuGroup() {
        // given
        Menu menu = new Menu();
        menu.setName("뿌링클치킨");
        menu.setPrice(BigDecimal.valueOf(17000));
        menu.setMenuGroupId(-1L);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(10);
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 존재하지 않는 Product라면 예외를 반환한다.")
    void create_WhenNotExistProduct() {
        // given
        Menu menu = new Menu();
        menu.setName("뿌링클치킨");
        menu.setPrice(BigDecimal.valueOf(17000));
        menu.setMenuGroupId(2L);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(-1L);
        menuProduct.setQuantity(10);
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 Product의 수량 * 합보다 가격이 비싸면 예외를 반환한다.")
    void create_WhenMoreThanSumPrice() {
        // given
        Menu menu = new Menu();
        menu.setName("뿌링클치킨");
        menu.setPrice(BigDecimal.valueOf(17000));
        menu.setMenuGroupId(2L);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(-1L);
        menuProduct.setQuantity(1);
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus.size()).isEqualTo(6);
    }
}
