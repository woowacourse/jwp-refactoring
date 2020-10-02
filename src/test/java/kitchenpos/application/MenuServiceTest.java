package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static kitchenpos.DomainFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@SpringBootTest
class MenuServiceTest {
    private static final String DELETE_MENU_GROUPS = "delete from menu_group where id in (:ids)";
    private static final String DELETE_MENU = "delete from menu where id in (:ids)";
    private static final String DELETE_PRODUCT = "delete from product where id in (:ids)";
    private static final String DELETE_MENU_PRODUCT = "delete from menu_product where seq in (:seqs)";
    private static final int BIG_DECIMAL_FLOOR_SCALE = 2;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    private MenuService menuService;

    private List<Long> menuGroupIds;
    private List<Long> productIds;
    private List<Long> menuIds;
    private List<Long> menuProductSeqs;

    static Stream<Arguments> invalidPrices() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(BigDecimal.valueOf(0)),
                Arguments.of(BigDecimal.valueOf(-1000))
        );
    }

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
        menuGroupIds = new ArrayList<>();
        productIds = new ArrayList<>();
        menuIds = new ArrayList<>();
        menuProductSeqs = new ArrayList<>();
    }

    @DisplayName("새로운 메뉴 저장")
    @Test
    void createTest() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        menuGroupIds.add(savedMenuGroup.getId());

        Product friedProduct = createProduct("후라이드치킨", BigDecimal.valueOf(16_000));
        Product sourceProduct = createProduct("양념치킨", BigDecimal.valueOf(16_000));
        Product savedFriedProduct = productDao.save(friedProduct);
        Product savedSourceProduct = productDao.save(sourceProduct);
        productIds.add(savedFriedProduct.getId());
        productIds.add(savedSourceProduct.getId());

        MenuProduct friedMenuProduct = createMenuProduct(savedFriedProduct.getId(), 1L);
        MenuProduct sourceMenuProduct = createMenuProduct(savedSourceProduct.getId(), 1L);
        List<MenuProduct> menuProducts = Arrays.asList(friedMenuProduct, sourceMenuProduct);

        Menu menu = createMenuWithMenuProducts(savedMenuGroup.getId(), "후라이드치킨+양념치킨",
                BigDecimal.valueOf(32_000), menuProducts);

        Menu createdMenu = menuService.create(menu);
        menuIds.add(createdMenu.getId());
        createdMenu.getMenuProducts().forEach(menuProduct -> menuProductSeqs.add(menuProduct.getSeq()));

        assertAll(
                () -> assertThat(createdMenu.getId()).isNotNull(),
                () -> assertThat(createdMenu.getMenuGroupId()).isEqualTo(savedMenuGroup.getId()),
                () -> assertThat(createdMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(createdMenu.getPrice()).isEqualTo(
                        menu.getPrice().setScale(BIG_DECIMAL_FLOOR_SCALE, BigDecimal.ROUND_FLOOR)),
                () -> assertThat(createdMenu.getMenuProducts()).hasSize(2)
        );
    }

    @DisplayName("새로운 메뉴 저장 시 가격을 잘못 입력했을 때 예외 출력")
    @ParameterizedTest
    @MethodSource("invalidPrices")
    void createWithInvalidPriceTest(BigDecimal price) {
        Menu menu = new Menu();
        menu.setPrice(price);

        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴 저장 시 존재하지 않는 메뉴 그룹 아이디 입력 시 예외 출력")
    @Test
    void createWithInvalidMenuGroupIdTest() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(10_000));
        menu.setMenuGroupId(0L);

        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴 저장 시 잘못된 메뉴 상품 입력 시 예외 출력")
    @Test
    void createMenuWithInvalidMenuProductTest() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        menuGroupIds.add(savedMenuGroup.getId());

        MenuProduct menuProduct = createMenuProduct(0L, 1L);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

        Menu menu = createMenuWithMenuProducts(savedMenuGroup.getId(), "잘못된 메뉴",
                BigDecimal.valueOf(10_000), menuProducts);

        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴 저장 시 입력한 메뉴의 가격이 메뉴 상품 가격의 총 합보다 더 클 때 예외 출력")
    @Test
    void createMenuWithInvalidMenuProductPriceSumTest() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        menuGroupIds.add(savedMenuGroup.getId());

        Product friedProduct = createProduct("후라이드치킨", BigDecimal.valueOf(16_000));
        Product savedFriedProduct = productDao.save(friedProduct);
        productIds.add(savedFriedProduct.getId());

        MenuProduct friedMenuProduct = createMenuProduct(savedFriedProduct.getId(), 1L);
        List<MenuProduct> menuProducts = Collections.singletonList(friedMenuProduct);

        Menu menu = createMenuWithMenuProducts(savedMenuGroup.getId(), "후라이드치킨+양념치킨",
                BigDecimal.valueOf(20_000), menuProducts);

        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장되어있는 모든 메뉴 조회")
    @Test
    void listTest() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        menuGroupIds.add(savedMenuGroup.getId());

        Product friedProduct = createProduct("후라이드치킨", BigDecimal.valueOf(16_000));
        Product savedFriedProduct = productDao.save(friedProduct);
        productIds.add(savedFriedProduct.getId());

        Menu friedMenu = createMenu(savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));
        Menu sourceMenu = createMenu(savedMenuGroup.getId(), "양념치킨", BigDecimal.valueOf(16_000));
        Menu savedFriedMenu = menuDao.save(friedMenu);
        Menu savedSourceMenu = menuDao.save(sourceMenu);
        menuIds.add(savedFriedMenu.getId());
        menuIds.add(savedSourceMenu.getId());

        MenuProduct friedMenuProduct = createMenuProduct(savedFriedMenu.getId(), savedFriedProduct.getId(), 1L);
        MenuProduct sourceMenuProduct = createMenuProduct(savedSourceMenu.getId(), savedFriedProduct.getId(), 1L);
        MenuProduct savedFriedMenuProduct = menuProductDao.save(friedMenuProduct);
        MenuProduct savedSourceMenuProduct = menuProductDao.save(sourceMenuProduct);
        menuProductSeqs.add(savedFriedMenuProduct.getSeq());
        menuProductSeqs.add(savedSourceMenuProduct.getSeq());

        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        deleteMenuProduct();
        deleteMenu();
        deleteProduct();
        deleteMenuGroup();
    }

    private void deleteMenuProduct() {
        Map<String, Object> params = Collections.singletonMap("seqs", menuProductSeqs);
        namedParameterJdbcTemplate.update(DELETE_MENU_PRODUCT, params);
    }

    private void deleteMenu() {
        Map<String, Object> params = Collections.singletonMap("ids", menuIds);
        namedParameterJdbcTemplate.update(DELETE_MENU, params);
    }

    private void deleteProduct() {
        Map<String, Object> params = Collections.singletonMap("ids", productIds);
        namedParameterJdbcTemplate.update(DELETE_PRODUCT, params);
    }

    private void deleteMenuGroup() {
        Map<String, Object> params = Collections.singletonMap("ids", menuGroupIds);
        namedParameterJdbcTemplate.update(DELETE_MENU_GROUPS, params);
    }
}