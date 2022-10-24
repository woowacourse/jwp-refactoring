package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuProductDao menuProductDao;

    private MenuGroup menuGroup;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("테스트");
        this.menuGroup = menuGroupDao.save(menuGroup);

        final Product product1 = new Product();
        product1.setName("상품1");
        product1.setPrice(new BigDecimal(1));
        final Product product2 = new Product();
        product2.setName("상품2");
        product2.setPrice(new BigDecimal(2));
        final Product product3 = new Product();
        product3.setName("상품3");
        product3.setPrice(new BigDecimal(3));

        this.product1 = productDao.save(product1);
        this.product2 = productDao.save(product2);
        this.product3 = productDao.save(product3);
    }

    @Test
    @DisplayName("메뉴를 추가할 수 있다.")
    void create() {
        final Menu menu1 = new Menu();
        menu1.setPrice(new BigDecimal(0));
        menu1.setMenuGroupId(menuGroup.getId());
        menu1.setName("메뉴1");
        menu1.setMenuProducts(new ArrayList<>());
        final Menu menu2 = new Menu();
        menu2.setPrice(new BigDecimal(0));
        menu2.setMenuGroupId(menuGroup.getId());
        menu2.setName("메뉴1");
        menu2.setMenuProducts(new ArrayList<>());

        final Menu savedMenu1 = menuService.create(menu1);
        final Menu savedMenu2 = menuService.create(menu2);

        final List<Menu> findMenus = menuService.list();

        assertThat(findMenus).usingElementComparatorIgnoringFields()
                .contains(savedMenu1, savedMenu2);
    }

    @Test
    @DisplayName("메뉴의 가격은 null일 수 없다.")
    void createWithNullPrice() {
        final Menu menu = new Menu();
        menu.setPrice(null);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName("메뉴");
        menu.setMenuProducts(new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격은 0보다 작을 수 없다.")
    void createWithUnderZeroPrice() {
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(-1));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName("메뉴");
        menu.setMenuProducts(new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 이름은 null일 수 없다.")
    void createWithNullName() {
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(1));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName(null);
        menu.setMenuProducts(new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되는 메뉴는 메뉴 그룹에 반드시 포함되어야 한다.")
    void createWithNullMenuGroup() {
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(1));
        menu.setMenuGroupId(null);
        menu.setName("메뉴");
        menu.setMenuProducts(new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 등록할 때 메뉴에 포함되는 메뉴상품은 모두 상품목록에 존재해야한다.")
    void createWithNotExistProduct() {
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(1));
        menu.setMenuGroupId(null);
        menu.setName("메뉴");
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(9999L);
        menuProduct.setQuantity(10L);
        menu.setMenuProducts(List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격은 메뉴 상품의 가격 합보다 더 클 수 없다.")
    void createWithOverPrice() {
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(10));
        menu.setMenuGroupId(null);
        menu.setName("메뉴");
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(product1.getId());
        menuProduct1.setQuantity(10L);
        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(product2.getId());
        menuProduct2.setQuantity(10L);
        final MenuProduct menuProduct3 = new MenuProduct();
        menuProduct3.setProductId(product3.getId());
        menuProduct3.setQuantity(10L);
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2, menuProduct3));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴가 등록될 때 메뉴 상품도 같이 등록한다.")
    void createWithMenuProducts() {
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(6));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName("메뉴");
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(product1.getId());
        menuProduct1.setQuantity(10L);
        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(product2.getId());
        menuProduct2.setQuantity(10L);
        final MenuProduct menuProduct3 = new MenuProduct();
        menuProduct3.setProductId(product3.getId());
        menuProduct3.setQuantity(10L);
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2, menuProduct3));

        final Menu savedMenu = menuService.create(menu);

        final List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(savedMenu.getId());

        assertThat(menuProducts).usingElementComparatorIgnoringFields("seq")
                .contains(menuProduct1, menuProduct2, menuProduct3);
    }
}
