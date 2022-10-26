package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Sql("/schema.sql")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private List<MenuProduct> menuProducts;
    private MenuProduct menuProduct;
    private Menu menu;

    @BeforeEach
    void setUp() {
        final Product product = new Product();
        product.setId(1L);
        product.setName("피자");
        product.setPrice(BigDecimal.valueOf(20_000L));
        productDao.save(product);

        menuProducts = new ArrayList<>();
        menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setQuantity(1);
        menuProducts.add(menuProduct);

        menu = new Menu();
        menu.setId(1L);
        menu.setName("애기메뉴");
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(20_000L));

        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("애기메뉴들");
        menuGroupDao.save(menuGroup);
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() {
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);
        final Menu createMenu = menuService.create(menu);

        assertThat(createMenu.getName())
                .isEqualTo("애기메뉴");
    }

    @Test
    @DisplayName("메뉴 가격이 0 미만이면 예외를 반환한다")
    void create_priceException() {
        menu.setPrice(BigDecimal.valueOf(-1L));
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 상품 가격의 합보다 크면 예외를 반환한다")
    void create_priceExpensiveException() {
        menu.setPrice(BigDecimal.valueOf(999999999L));
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 포함된 상품이 존재하지 않는 상품이면 예외를 반환한다")
    void create_notExistProductException() {
        menu.setMenuGroupId(1000L);
        menu.setPrice(BigDecimal.valueOf(999999999L));
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 전체를 조회한다")
    void list() {
        menuDao.save(menu);

        final List<Menu> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual)
                        .extracting("id")
                        .containsExactly(1L)
        );
    }
}
