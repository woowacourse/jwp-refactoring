package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private Menu menu;

    @BeforeEach
    void setMenu() {
        Product product = new Product();
        product.setName("product1");
        product.setPrice(BigDecimal.ONE);
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(persistProduct.getId());
        menuProduct.setQuantity(10);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        menu = new Menu();
        menu.setPrice(BigDecimal.TEN);
        menu.setName("menu1");
        menu.setMenuGroupId(persistMenuGroup.getId());
        menu.setMenuProducts(Arrays.asList(menuProduct));
    }

    @Test
    void create() {
        Menu persistMenu = menuService.create(menu);

        assertAll(
            () -> assertThat(persistMenu.getId()).isNotNull(),
            () -> assertThat(persistMenu).isEqualToComparingOnlyGivenFields(menu, "name", "menuGroupId"),
            () -> assertThat(persistMenu.getPrice().longValue()).isEqualTo(menu.getPrice().longValue()),
            () -> assertThat(persistMenu.getMenuProducts()).usingElementComparatorIgnoringFields("seq")
                .isEqualTo(menu.getMenuProducts())
        );
    }

    @Test
    void list() {
        Menu persistMenu = menuService.create(menu);
        List<Menu> menus = menuService.list();
        List<String> menuNames = menus.stream()
            .map(Menu::getName)
            .collect(Collectors.toList());

        assertThat(menuNames).contains(persistMenu.getName());
    }
}