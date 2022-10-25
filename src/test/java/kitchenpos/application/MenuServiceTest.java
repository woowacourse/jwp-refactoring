package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private Product product;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        databaseCleaner.tableClear();

        product = productDao.save(new Product("치킨", BigDecimal.valueOf(10000)));
        menuGroup = menuGroupDao.save(new MenuGroup("1번 메뉴 그룹"));
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        Menu newMenu = new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(),
                createMenuProducts(product.getId()));

        Menu menu = menuService.create(newMenu);

        assertThat(menu).isNotNull();
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격이 null이면 예외가 발생한다.")
    @Test
    void createWithNullPrice() {
        Menu menu = new Menu("1번 메뉴", null, menuGroup.getId(), createMenuProducts(product.getId()));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격이 0보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -5})
    void createWithInvalidPrice(int price) {
        Menu menu = new Menu("1번 메뉴", BigDecimal.valueOf(price), menuGroup.getId(),
                createMenuProducts(product.getId()));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void createWithNoMenuGroup() {
        Menu menu = new Menu("1번 메뉴", BigDecimal.valueOf(10000), 9999L, createMenuProducts(product.getId()));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴상품이 비어 있으면 예외가 발생한다.")
    @Test
    void createWithNoMenuProduct() {
        Menu menu = new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격이 메뉴 상품의 총합 보다 크면 예외가 발생한다.")
    @ValueSource(ints = {10001, 50000})
    @ParameterizedTest
    void createWithPriceMoreThanMenuProductSum(int price) {
        Menu menu = new Menu("1번 메뉴", BigDecimal.valueOf(price), menuGroup.getId(),
                createMenuProducts(product.getId()));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴들을 조회할 수 있다.")
    @Test
    void list() {
        Menu menu = new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(),
                createMenuProducts(product.getId()));
        menuDao.save(menu);

        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(1);
    }

    private List<MenuProduct> createMenuProducts(Long... productIds) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (Long productId : productIds) {
            menuProducts.add(new MenuProduct(productId, 1L));
        }
        return menuProducts;
    }
}
