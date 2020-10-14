package kitchenpos.application;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest(classes = {
        JdbcTemplateMenuDao.class,
        JdbcTemplateMenuGroupDao.class,
        JdbcTemplateMenuProductDao.class,
        JdbcTemplateProductDao.class,
        MenuService.class
})
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    private MenuGroup menuGroup;

    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        final Product product = productDao.save(createProduct("매콤치킨", BigDecimal.valueOf(16000)));
        menuGroup = menuGroupDao.save(createMenuGroup("이십마리메뉴"));

        final Menu menu = menuDao.save(createMenu("마늘간장치킨", BigDecimal.valueOf(16000), menuGroup.getId(), Collections.emptyList()));
        menuProduct = menuProductDao.save(creatMenuProduct(menu.getId(), product.getId(), 1L));
    }

    @DisplayName("create: 메뉴 생성")
    @Test
    void create() {
        final Menu menu = createMenu("후라이드치킨", BigDecimal.valueOf(16000), menuGroup.getId(), Collections.singletonList(menuProduct));
        final Menu actual = menuService.create(menu);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo("후라이드치킨");
        assertThat(actual.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(16000));
        assertThat(actual.getMenuGroupId()).isEqualTo(menuGroup.getId());
        assertThat(actual.getMenuProducts()).isNotEmpty();
    }

    @DisplayName("create: 가격이 null일 때 예외 처리")
    @Test
    void create_IfPriceIsNull_Exception() {
        final Menu menu = createMenu("후라이드치킨", null, menuGroup.getId(), Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 가격이 음수일 때 예외 처리")
    @Test
    void create_IfPriceIsNegative_Exception() {
        final Menu menu = createMenu("후라이드치킨", BigDecimal.valueOf(-1L), menuGroup.getId(), Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: MenuGroup의 Id가 존재하지 않을 때 예외 처리")
    @Test
    void create_IfMenuGroupIdDoesNotExist_Exception() {
        final Menu menu = createMenu("후라이드치킨", BigDecimal.valueOf(16000),0L, Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 상품의 Id가 존재하지 않을 때 예외 처리")
    @Test
    void create_IfProductIdDoesNotExist_InMenuProduct_Exception() {
        final MenuProduct menuProductNotExistId = creatMenuProduct(1L, 0L, 1L);
        final Menu menu = createMenu("후라이드치킨", BigDecimal.valueOf(16000), menuGroup.getId(), Collections.singletonList(menuProductNotExistId));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 가격이 메뉴상품의 가격의 총 합보다 클 때 예외 처리")
    @Test
    void create_IfPriceIsGreaterThanSumOfMenuProduct_Exception() {
        final Menu menu = createMenu("후라이드치킨", BigDecimal.valueOf(Integer.MAX_VALUE), menuGroup.getId(), Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list: 메뉴 전체 조회")
    @Test
    void list() {
        final List<Menu> menus = menuService.list();

        assertThat(menus).isNotEmpty();
    }
}