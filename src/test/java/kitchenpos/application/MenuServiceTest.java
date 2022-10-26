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
        final Product product = new Product(1L, "피자", BigDecimal.valueOf(20_000L));
        productDao.save(product);

        menuProducts = new ArrayList<>();
        menuProduct = new MenuProduct(1L, 1L, 1L, 5);
        menuProducts.add(menuProduct);

        menu = new Menu(1L, "애기메뉴", BigDecimal.valueOf(20_000L), 1L, new ArrayList<>());

        final MenuGroup menuGroup = new MenuGroup(1L, "애기메뉴들");
        menuGroupDao.save(menuGroup);
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() {
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);
        final Menu createMenu = menuService.create("애기메뉴", BigDecimal.valueOf(2_000L), 1L, menuProducts);

        assertThat(createMenu.getName())
                .isEqualTo("애기메뉴");
    }

    @Test
    @DisplayName("메뉴 가격이 0 미만이면 예외를 반환한다")
    void create_priceException() {
        assertThatThrownBy(() -> menu.changePrice(BigDecimal.valueOf(-1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 상품 가격의 합보다 크면 예외를 반환한다")
    void create_priceExpensiveException() {
        menu.changePrice(BigDecimal.valueOf(999999999L));
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create("애기메뉴", BigDecimal.valueOf(20_000L), 1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 포함된 상품이 존재하지 않는 상품이면 예외를 반환한다")
    void create_notExistProductException() {
        menu.setMenuGroupId(1000L);
        menu.changePrice(BigDecimal.valueOf(999999999L));
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create("애기메뉴", BigDecimal.valueOf(20_000L), 1L, new ArrayList<>()))
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
