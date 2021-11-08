package kitchenpos;

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("MenuService 테스트")
class MenuServiceTest {

    private static final long ID = 1L;
    private static final long MENU_GROUP_ID = 1L;
    private static final BigDecimal PRICE = BigDecimal.valueOf(20_000);
    private static final long NO_EXISTS_MENU_GROUP_ID = 2L;
    private static final BigDecimal INVALID_PRICE = BigDecimal.valueOf(-1);

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;
    private MenuGroup menuGroup;

    @Autowired
    private ProductDao productDao;
    private Product product;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupFixture.create();
        menuGroup = menuGroupDao.save(menuGroup);

        product = ProductFixture.create();
        product = productDao.save(product);

        menuProducts = MenuFixture.menuProducts();
    }

    @DisplayName("메뉴 추가 - 성공")
    @Test
    void create() {
        //given
        //when
        Menu menu = MenuFixture.create();
        Menu createdMenu = menuService.create(menu);
        //then
        assertThat(createdMenu.getId()).isNotNull();
        assertThat(createdMenu.getMenuProducts()).isNotNull();
    }

    @DisplayName("메뉴 추가 - 실패 - 유효한 가격이 아닌 경우")
    @Test
    void createFailureWhenInvalidPriceOrNullPrice() {
        //given
        Menu invalidMenu = MenuFixture.create(ID, "INVALID", INVALID_PRICE, MENU_GROUP_ID, menuProducts);
        Menu nullMenu = MenuFixture.create(ID, "INVALID", null, MENU_GROUP_ID, menuProducts);
        //when
        //then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> menuService.create(nullMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 추가 - 실패 - 존재하지 않는 MenuGroup인 경우")
    @Test
    void createFailureWhenInvalidMenuGroup() {
        Menu invalidMenu = MenuFixture.create(ID, "치킨", PRICE, NO_EXISTS_MENU_GROUP_ID, menuProducts);

        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 추가 - 실패 - Menu의 가격이 MenuProducts 가격의 합을 넘는 경우")
    @Test
    void createFailureWhenInvalidMenuGroupPrice() {
        Menu invalidMenu = MenuFixture.create(ID, "치킨", PRICE.add(BigDecimal.ONE), MENU_GROUP_ID, menuProducts);

        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴 반환")
    @Test
    void list() {
        //given
        Menu menu = MenuFixture.create();
        menuService.create(menu);
        //when
        List<Menu> menus = menuService.list();
        //then
        assertThat(menus).hasSize(1);
    }
}
