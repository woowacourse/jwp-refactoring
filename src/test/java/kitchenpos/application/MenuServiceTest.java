package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("Menu 서비스 테스트")
@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    private MenuGroup savedMenuGroup;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("메뉴 그룹");
        savedMenuGroup = menuGroupService.create(menuGroup);

        final Product product = new Product();
        product.setName("상품");
        product.setPrice(new BigDecimal(1000));
        savedProduct = productService.create(product);
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() {
        final Menu menu = new Menu();
        menu.setName("메뉴");
        menu.setPrice(new BigDecimal(1000));
        menu.setMenuGroupId(savedMenuGroup.getId());

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        menu.setMenuProducts(List.of(menuProduct));

        final Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isNotNull();
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 null 이 아니어야 한다")
    @Test
    void createMenuPriceIsNull() {
        final Menu menu = new Menu();
        menu.setName("메뉴");
        menu.setPrice(null);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 0원 이상이어야 한다")
    @Test
    void createMenuPriceIsLowerZero() {
        final Menu menu = new Menu();
        menu.setName("메뉴");
        menu.setPrice(new BigDecimal(-1));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 그룹의 아이디가 존재해야 한다")
    @Test
    void createMenuGroupIdIsNotExist() {
        final Menu menu = new Menu();
        menu.setName("메뉴");
        menu.setPrice(new BigDecimal(1000));

        long notSavedMenuGroupId = 0L;
        menu.setMenuGroupId(notSavedMenuGroupId);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 상품은 등록되어 있는 상품이어야 한다")
    @Test
    void createMenuProductIsNotExist() {
        final Menu menu = new Menu();
        menu.setName("메뉴");
        menu.setPrice(new BigDecimal(1000));
        menu.setMenuGroupId(savedMenuGroup.getId());

        final MenuProduct notSavedMenuProduct = new MenuProduct();
        menu.setMenuProducts(List.of(notSavedMenuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 주문 금액의 총합보다 작거나 같아야 한다")
    @Test
    void createMenuPriceIsHigherThanAmount() {
        final Menu menu = new Menu();
        menu.setName("메뉴");
        menu.setPrice(new BigDecimal(1200));
        menu.setMenuGroupId(savedMenuGroup.getId());

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        menu.setMenuProducts(List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회한다")
    @Test
    void list() {
        final List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(6);
    }
}
