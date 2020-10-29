package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.utils.DomainFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql(value = "/truncate.sql")
class MenuServiceTest {
    private static final String MENU_NAME = "후라이드치킨";
    private static final String PRODUCT_NAME = "후라이드치킨";

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

    @DisplayName("Menu 등록 성공")
    @Test
    void create() {
        BigDecimal price = new BigDecimal(10000);
        Product savedProduct = productDao.save(DomainFactory.createProduct(null, PRODUCT_NAME, price));
        MenuProduct menuProduct = DomainFactory.createMenuProduct(null, savedProduct.getId(), 2L);
        MenuGroup savedMenuGroup = menuGroupDao.save(DomainFactory.createMenuGroup("menuGroup"));
        Menu menu = DomainFactory.createMenu(null, MENU_NAME, price, savedMenuGroup.getId(), menuProduct);

        Menu actual = menuService.create(menu);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getName()).isEqualTo(MENU_NAME);
            assertThat(actual.getPrice().compareTo(price)).isEqualTo(0);
            assertThat(actual.getMenuGroupId()).isEqualTo(savedMenuGroup.getId());
            assertThat(actual.getMenuProducts()).extracting(MenuProduct::getSeq).isNotNull();
        });
    }

    @DisplayName("Menu 가격이 0보다 작은 경우 예외 테스트")
    @Test
    void createPriceLessThanZero() {
        Menu menu = DomainFactory.createMenu(null, MENU_NAME, BigDecimal.ZERO, null, null);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu GruopId가 존재하지 않는 경우 예외 테스트")
    @Test
    void createNotExistMenuGroupId() {
        Menu menu = DomainFactory.createMenu(null, MENU_NAME, BigDecimal.ONE, 1L, null);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 속하는 MenuProducts의 총 금액이 메뉴의 가격보다 작은 경우 예외 테스트")
    @Test
    void createMenuProductsAmountLessThanMenuPrice() {
        Product savedProduct = productDao.save(DomainFactory.createProduct(null, PRODUCT_NAME, new BigDecimal(9999)));
        MenuProduct menuProduct = DomainFactory.createMenuProduct(null, savedProduct.getId(), 1L);
        MenuGroup savedMenuGroup = menuGroupDao.save(DomainFactory.createMenuGroup("menuGroup"));
        Menu menu = DomainFactory.createMenu(null, MENU_NAME, new BigDecimal(10000), savedMenuGroup.getId(), menuProduct);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 전체조회 테스트")
    @Test
    void list() {
        BigDecimal price = new BigDecimal(10000);
        Product savedProduct = productDao.save(DomainFactory.createProduct(null, PRODUCT_NAME, price));
        MenuProduct menuProduct = DomainFactory.createMenuProduct(null, savedProduct.getId(), 2L);
        MenuGroup savedMenuGroup = menuGroupDao.save(DomainFactory.createMenuGroup("menuGroup"));
        Menu menu = DomainFactory.createMenu(null, MENU_NAME, price, savedMenuGroup.getId(), menuProduct);
        menuDao.save(menu);

        List<Menu> actual = menuService.list();

        assertThat(actual).hasSize(1);
    }
}