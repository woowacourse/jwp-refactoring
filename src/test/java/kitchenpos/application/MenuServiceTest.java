package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static kitchenpos.DomainFactory.createMenuProduct;
import static kitchenpos.DomainFactory.createMenuWithMenuProducts;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuServiceTest extends ServiceTest {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuService menuService;

    static Stream<Arguments> invalidPrices() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(BigDecimal.valueOf(-1000))
        );
    }

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);
    }

    @DisplayName("새로운 메뉴 저장")
    @Test
    void createTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Product savedFriedProduct = saveProduct(productRepository, "후라이드치킨", BigDecimal.valueOf(16_000));
        Product savedSourceProduct = saveProduct(productRepository, "양념치킨", BigDecimal.valueOf(16_000));

        MenuProduct friedMenuProduct = createMenuProduct(savedFriedProduct.getId(), 1L);
        MenuProduct sourceMenuProduct = createMenuProduct(savedSourceProduct.getId(), 1L);
        List<MenuProduct> menuProducts = Arrays.asList(friedMenuProduct, sourceMenuProduct);

        Menu menu = createMenuWithMenuProducts(savedMenuGroup.getId(), "후라이드치킨+양념치킨",
                BigDecimal.valueOf(32_000), menuProducts);

        Menu createdMenu = menuService.create(menu);

        assertAll(
                () -> assertThat(createdMenu.getId()).isNotNull(),
                () -> assertThat(createdMenu.getMenuGroup().getId()).isEqualTo(savedMenuGroup.getId()),
                () -> assertThat(createdMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(createdMenu.getPrice()).isEqualTo(menu.getPrice()),
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
        menu.setMenuGroup(new MenuGroup(0L));

        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴 저장 시 잘못된 메뉴 상품 입력 시 예외 출력")
    @Test
    void createMenuWithInvalidMenuProductTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");

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
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Product savedFriedProduct = saveProduct(productRepository, "후라이드치킨", BigDecimal.valueOf(16_000));

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
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Product savedFriedProduct = saveProduct(productRepository, "후라이드치킨", BigDecimal.valueOf(16_000));
        Menu savedFriedMenu = saveMenu(menuRepository, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));
        saveMenuProduct(menuProductRepository, savedFriedMenu.getId(), savedFriedProduct.getId(), 1L);

        List<Menu> menus = menuService.list();

        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menus.get(0).getMenuProducts()).hasSize(1)
        );
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        productRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}