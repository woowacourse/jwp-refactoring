package kitchenpos;

import kitchenpos.application.MenuService;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("MenuService 테스트")
class MenuServiceTest {

    private static final long NOT_EXISTS_MENUGROUP_ID = 999L;
    private static final int VALID_PRICE = 18_900;
    private static final int INVALID_MENU_PRICE1 = 19_000;
    private static final int INVALID_PRICE = -1;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;
    private MenuGroup menuGroup;

    @Autowired
    private ProductDao productDao;
    private Product product;
    private Product invalidProduct;

    private MenuProduct menuProduct;
    private MenuProduct invalidMenuProduct;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.builder()
                .name("이달의 메뉴")
                .build();
        menuGroup = menuGroupDao.save(menuGroup);

        product = Product.builder()
                .name("이달의 치킨")
                .price(BigDecimal.valueOf(VALID_PRICE))
                .build();
        invalidProduct = Product.builder()
                .name("잘못된 치킨")
                .price(BigDecimal.valueOf(VALID_PRICE))
                .build();
        product = productDao.save(product);

        menuProduct = MenuProduct.builder()
                .productId(product.getId())
                .quantity(1)
                .build();
        invalidMenuProduct = MenuProduct.builder()
                .productId(invalidProduct.getId())
                .quantity(99)
                .build();
    }

    @DisplayName("메뉴 추가 - 성공")
    @Test
    void create() {
        Menu menu = Menu.builder()
                .name("이달의 치킨")
                .price(BigDecimal.valueOf(VALID_PRICE))
                .menuGroupId(menuGroup.getId())
                .menuProducts(Arrays.asList(menuProduct))
                .build();

        Menu createdMenu = menuService.create(menu);

        assertThat(createdMenu.getId()).isNotNull();
        assertThat(createdMenu.getMenuProducts()).isNotNull();
    }

    @DisplayName("메뉴 추가 - 실패 - 유효한 가격이 아닌 경우")
    @Test
    void createFailureWhenInvalidPriceOrNullPrice() {
        Menu invalidMenu = Menu.builder()
                .name("이달의 치킨")
                .price(BigDecimal.valueOf(MenuServiceTest.INVALID_PRICE))
                .menuGroupId(menuGroup.getId())
                .menuProducts(Arrays.asList(menuProduct))
                .build();
        Menu nullMenu = Menu.builder()
                .name("이달의 치킨")
                .price(null)
                .menuGroupId(menuGroup.getId())
                .menuProducts(Arrays.asList(menuProduct))
                .build();

        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> menuService.create(nullMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 추가 - 실패 - 존재하지 않는 MenuGroup인 경우")
    @Test
    void createFailureWhenInvalidMenuGroup() {
        Menu invalidMenu = Menu.builder()
                .name("이달의 치킨")
                .price(BigDecimal.valueOf(VALID_PRICE))
                .menuGroupId(NOT_EXISTS_MENUGROUP_ID)
                .menuProducts(Arrays.asList(invalidMenuProduct))
                .build();

        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 추가 - 실패 - Menu의 가격이 MenuProducts 가격의 합을 넘는 경우")
    @Test
    void createFailureWhenInvalidMenuGroupPrice() {
        Menu invalidMenu = Menu.builder()
                .name("이달의 치킨")
                .price(BigDecimal.valueOf(INVALID_MENU_PRICE1))
                .menuGroupId(NOT_EXISTS_MENUGROUP_ID)
                .menuProducts(Arrays.asList(invalidMenuProduct))
                .build();

        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴 반환")
    @Test
    void list() {
        //given
        Menu menu = Menu.builder()
                .name("이달의 치킨")
                .price(BigDecimal.valueOf(VALID_PRICE))
                .menuGroupId(menuGroup.getId())
                .menuProducts(Arrays.asList(menuProduct))
                .build();
        menuService.create(menu);
        //when
        List<Menu> menus = menuService.list();
        //then
        assertThat(menus).isNotEmpty();
    }
}
