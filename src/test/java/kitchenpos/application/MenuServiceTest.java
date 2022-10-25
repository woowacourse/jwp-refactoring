package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MenuServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴를 조회한다.")
    @Test
    void list() {
        Product product = givenProduct("강정치킨", 17000);
        MenuGroup menuGroup = givenMenuGroup("라라 메뉴");
        MenuProduct menuProduct = givenMenuProduct(product.getId(), 1);

        Menu menu = givenMenu("해장 세트", 15000, menuGroup.getId(), List.of(menuProduct));
        Menu savedMenu = menuService.create(menu);

        List<Menu> menus = menuService.list();

        assertThat(menus).extracting(Menu::getId, Menu::getName, p -> p.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(savedMenu.getId(), "해장 세트", 15000)
                );
    }

    private Product givenProduct(String name, long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return productService.create(product);
    }

    private MenuGroup givenMenuGroup(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroupService.create(menuGroup);
    }

    private MenuProduct givenMenuProduct(long productId, int quantity) {
        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(productId);
        menuProduct1.setQuantity(quantity);
        return menuProduct1;
    }

    private Menu givenMenu(String name, int price, long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
