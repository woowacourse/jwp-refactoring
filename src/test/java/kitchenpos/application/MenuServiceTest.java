package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.utils.KitchenposClassCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@Sql("/truncate.sql")
@SpringBootTest
public class MenuServiceTest {
    public static final String TEST_MENU_GROUP_NAME = "두마리 세트";
    public static final long TEST_MENU_PRODUCT_QUANTITY = 1L;
    private static final String TEST_MENU_NAME = "후라이드 치킨";
    private static final BigDecimal TEST_MENU_PRICE = new BigDecimal("16000.00");
    private static final String TEST_PRODUCT_NAME_1 = "후라이드 치킨";
    private static final String TEST_PRODUCT_NAME_2 = "코카콜라";
    private static final BigDecimal TEST_PRODUCT_PRICE_1 = new BigDecimal("15000.00");
    private static final BigDecimal TEST_PRODUCT_PRICE_2 = new BigDecimal("1000.00");
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuService menuService;

    private Menu menu;
    private Product product1;
    private Product product2;
    private List<MenuProduct> menuProducts;


    @BeforeEach
    void setUp() {
        product1 = KitchenposClassCreator.createProduct(TEST_PRODUCT_NAME_1, TEST_PRODUCT_PRICE_1);
        product2 = KitchenposClassCreator.createProduct(TEST_PRODUCT_NAME_2, TEST_PRODUCT_PRICE_2);

        product1 = productDao.save(product1);
        product2 = productDao.save(product2);

        MenuGroup menuGroup = KitchenposClassCreator.createMenuGroup(TEST_MENU_GROUP_NAME);
        menuGroup = menuGroupDao.save(menuGroup);

        MenuProduct menuProduct1 = KitchenposClassCreator.createMenuProduct(product1, TEST_MENU_PRODUCT_QUANTITY);
        MenuProduct menuProduct2 = KitchenposClassCreator.createMenuProduct(product2, TEST_MENU_PRODUCT_QUANTITY);

        menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        menu = KitchenposClassCreator.createMenu(TEST_MENU_NAME, menuGroup, TEST_MENU_PRICE, menuProducts);
    }

    @DisplayName("Menu 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        Menu savedMenu = menuService.create(menu);
        List<MenuProduct> savedMenuProducts = savedMenu.getMenuProducts();

        Assertions.assertEquals(savedMenu.getMenuGroupId(), menu.getMenuGroupId());
        Assertions.assertEquals(savedMenu.getName(), menu.getName());
        Assertions.assertEquals(savedMenu.getPrice(), menu.getPrice());
        assertThat(savedMenuProducts)
                .hasSize(menuProducts.size())
                .extracting("productId")
                .containsOnly(product1.getId(), product2.getId());
    }

    @DisplayName("Menu 전체 목록을 요청 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        Menu savedMenu = menuService.create(menu);

        List<Menu> foundMenus = menuService.list();
        Menu foundMenu = foundMenus.get(0);

        assertThat(foundMenus).hasSize(1);
        assertThat(foundMenu.getId()).isEqualTo(savedMenu.getId());
        assertThat(foundMenu.getPrice()).isEqualTo(savedMenu.getPrice());
        assertThat(foundMenu.getMenuGroupId()).isEqualTo(savedMenu.getMenuGroupId());
        assertThat(foundMenu.getName()).isEqualTo(savedMenu.getName());
    }

    @DisplayName("예외 테스트 : 가격이 0 이하일 경우, IllegalArgumentException이 발생한다.")
    @ValueSource(longs = {-1000000, -42, -1})
    @ParameterizedTest
    void createWithNegativePriceExceptionTest(long source) {
        BigDecimal invalidPrice = BigDecimal.valueOf(source);
        menu.setPrice(invalidPrice);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : 가격이 NULL일 경우, IllegalArgumentException이 발생한다.")
    @Test
    void createWithNullPriceExceptionTest() {
        menu.setPrice(null);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : MenuGroup을 DB에서 찾을 수 없는 경우, IllegalArgumentException이 발생한다.")
    @Test
    void createWithInvalidMenuGroupIdExceptionTest() {
        Long invalidMenuGroupId = 100000L;
        menu.setMenuGroupId(invalidMenuGroupId);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : 존재하지 않는 상품이 MenuProduct에 포함될 경우, IllegalArgumentException이 발생한다.")
    @Test
    void createWithInvalidProductExceptionTest() {
        Long invalidProductId = 100000L;
        menuProducts.get(0).setProductId(invalidProductId);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : 모든 상품 가격의 합이 메뉴 가격보다 낮은 경우, IllegalArgumentException이 발생한다.")
    @Test
    void createWithInvalidPriceSumExceptionTest() {
        //기존의 15000원짜리 상품을 2000원으로 교체한다.
        BigDecimal invalidPrice = BigDecimal.valueOf(2000L);
        product1.setPrice(invalidPrice);
        Product savedProduct = productDao.save(product1);
        menuProducts.get(0).setProductId(savedProduct.getId());
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
