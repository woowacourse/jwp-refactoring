package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.createMenu;
import static kitchenpos.helper.EntityCreateHelper.createMenuGroup;
import static kitchenpos.helper.EntityCreateHelper.createMenuProduct;
import static kitchenpos.helper.EntityCreateHelper.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = "/truncate.sql")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        Product product = createProduct(null, "콜라", BigDecimal.valueOf(2000L));
        Product savedProduct = productDao.save(product);

        MenuGroup menuGroup = createMenuGroup(null, "음료류");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        MenuProduct menuProduct = createMenuProduct(null, savedProduct.getId(), 1L);
        Menu menu = createMenu(null, savedMenuGroup.getId(), Arrays.asList(menuProduct), "콜라 세트",
            BigDecimal.valueOf(1900L));

        Menu savedMenu = menuService.create(menu);

        assertAll(
            () -> assertThat(savedMenu.getId()).isNotNull(),
            () -> assertThat(savedMenu.getPrice().longValue()).isEqualTo(1900L),
            () -> assertThat(savedMenu.getName()).isEqualTo("콜라 세트"),
            () -> assertThat(savedMenu.getMenuProducts().get(0).getQuantity()).isEqualTo(1L)
        );
    }

    @DisplayName("메뉴 가격이 음수거나 null일 경우 예외가 발생하는지 확인한다.")
    @Test
    void createInvalidPriceMenu() {
        Menu menuWithNullPrice = new Menu();
        menuWithNullPrice.setPrice(null);

        Menu menuWithMinusPrice = new Menu();
        menuWithMinusPrice.setPrice(BigDecimal.valueOf(-1L));

        assertAll(
            () -> assertThatThrownBy(
                () -> menuService.create(menuWithNullPrice)
            ).isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(
                () -> menuService.create(menuWithMinusPrice)
            ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("메뉴가 아무 메뉴 그룹에도 속하지 않을 시 예외가 발생하는지 확인한다.")
    @Test
    void createInvalidMenuGroup() {
        MenuGroup menuGroup = createMenuGroup(null, "음료류");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = createMenu(1L, savedMenuGroup.getId(), Arrays.asList(), "둘둘치킨", BigDecimal.valueOf(2000L));

        assertThatThrownBy(
            () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 개별 상품 가격의 총합보다 비쌀 시 예외가 발생하는지 확인한다.")
    @Test
    void createInvalidDiscountMenu() {
        Product product = createProduct(1L, "콜라", BigDecimal.valueOf(2000L));
        MenuProduct menuProduct = createMenuProduct(null, 2L, 1L);
        Menu menu = createMenu(1L, 1L, Arrays.asList(menuProduct), "콜라세트", BigDecimal.valueOf(2100L));
        MenuGroup menuGroup = createMenuGroup(null, "음료류");

        menuGroupDao.save(menuGroup);
        productDao.save(product);

        assertThatThrownBy(
            () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴그룹을 조회한다.")
    @Test
    void list() {
        Product product = createProduct(null, "콜라", BigDecimal.valueOf(2000L));
        Product savedProduct = productDao.save(product);

        MenuGroup menuGroup = createMenuGroup(null, "음료류");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        MenuProduct menuProduct = createMenuProduct(null, savedProduct.getId(), 1L);
        Menu menu = createMenu(null, savedMenuGroup.getId(), Arrays.asList(menuProduct), "콜라 세트",
            BigDecimal.valueOf(1900L));

        menuService.create(menu);
        List<Menu> menus = menuService.list();

        assertAll(
            () -> assertThat(menus.size()).isEqualTo(1),
            () -> assertThat(menus.get(0).getId()).isNotNull(),
            () -> assertThat(menus.get(0).getPrice().longValue()).isEqualTo(1900L),
            () -> assertThat(menus.get(0).getName()).isEqualTo("콜라 세트")
        );
    }
}
