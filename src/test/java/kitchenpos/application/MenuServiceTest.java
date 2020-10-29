package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.utils.KitchenPosClassCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.ProductServiceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Sql("/truncate.sql")
@SpringBootTest
public class MenuServiceTest {
    private static final String 두마리_세트 = "두마리 세트";
    private static final long 메뉴_1개 = 1L;
    private static final String 메뉴_후라이드_치킨 = "후라이드 치킨";
    private static final BigDecimal 메뉴_16000원 = new BigDecimal("16000.00");
    public static final int 메뉴_잘못된_개수 = -1;
    private static final String 후라이드_치킨 = "후라이드 치킨";
    private static final String 코카콜라 = "코카콜라";
    private static final BigDecimal 가격_15000원 = new BigDecimal("15000.00");
    private static final BigDecimal 가격_1000원 = new BigDecimal("1000.00");

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
        product1 = KitchenPosClassCreator.createProduct(후라이드_치킨, 가격_15000원);
        product2 = KitchenPosClassCreator.createProduct(코카콜라, 가격_1000원);

        product1 = productDao.save(product1);
        product2 = productDao.save(product2);

        MenuGroup menuGroup = KitchenPosClassCreator.createMenuGroup(두마리_세트);
        menuGroup = menuGroupDao.save(menuGroup);

        MenuProduct menuProduct1 = KitchenPosClassCreator.createMenuProduct(product1, 메뉴_1개);
        MenuProduct menuProduct2 = KitchenPosClassCreator.createMenuProduct(product2, 메뉴_1개);

        menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        menu = KitchenPosClassCreator.createMenu(메뉴_후라이드_치킨, menuGroup, 메뉴_16000원, menuProducts);
    }

    @DisplayName("Menu 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        Menu savedMenu = menuService.create(menu);
        List<MenuProduct> savedMenuProducts = savedMenu.getMenuProducts();

        assertEquals(savedMenu.getMenuGroupId(), menu.getMenuGroupId());
        assertEquals(savedMenu.getName(), menu.getName());
        assertEquals(savedMenu.getPrice(), menu.getPrice());
        assertThat(savedMenuProducts)
                .hasSize(menuProducts.size())
                .extracting("productId")
                .containsOnly(product1.getId(), product2.getId());
    }

    @DisplayName("예외 테스트 : Menu 생성 중 가격이 0 이하일 경우, 예외가 발생한다.")
    @Test
    void createWithNegativePriceExceptionTest() {
        BigDecimal invalidPrice = BigDecimal.valueOf(메뉴_잘못된_개수);
        menu.setPrice(invalidPrice);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : Menu 생성 중 가격이 NULL일 경우, 예외가 발생한다.")
    @Test
    void createWithNullPriceExceptionTest() {
        menu.setPrice(null);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : Menu 생성 중 MenuGroup을 DB에서 찾을 수 없는 경우, 예외가 발생한다.")
    @Test
    void createWithInvalidMenuGroupIdExceptionTest() {
        Long invalidMenuGroupId = 100000L;
        menu.setMenuGroupId(invalidMenuGroupId);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : Menu 생성 중 존재하지 않는 상품이 MenuProduct에 포함될 경우, 예외가 발생한다.")
    @Test
    void createWithInvalidProductExceptionTest() {
        Long invalidProductId = 100000L;
        menuProducts.get(0).setProductId(invalidProductId);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : Menu 생성 중 모든 상품 가격의 합이 메뉴 가격보다 낮은 경우, 예외가 발생한다.")
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

    @DisplayName("Menu 전체 목록을 요청 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        Menu savedMenu = menuService.create(menu);

        List<Menu> foundMenus = menuService.list();
        Menu foundMenu = foundMenus.get(0);

        assertThat(foundMenus).hasSize(1);
        assertEquals(foundMenu.getId(), savedMenu.getId());
        assertEquals(foundMenu.getPrice(), savedMenu.getPrice());
        assertEquals(foundMenu.getMenuGroupId(), savedMenu.getMenuGroupId());
        assertEquals(foundMenu.getName(), savedMenu.getName());
    }
}
