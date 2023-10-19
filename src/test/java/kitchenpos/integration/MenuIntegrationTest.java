package kitchenpos.integration;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuIntegrationTest extends IntegrationTest {

    @Test
    void 메뉴_생성을_요청한다() {
        // given
        // 상품 생성
        final Product chicken = createProduct("chicken", 500);
        final Product pizza = createProduct("pizza", 500);

        // 메뉴 그룹 생성
        final MenuGroup menuGroup = createMenuGroup("외식류");

        // 메뉴 생성
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(chicken.getId());
        menuProduct1.setQuantity(1);

        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(pizza.getId());
        menuProduct2.setQuantity(1);

        final Menu menu = new Menu();
        menu.setName("치킨 + 피자");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));
        final HttpEntity<Menu> request = new HttpEntity<>(menu);

        // when
        final ResponseEntity<Menu> response = testRestTemplate
                .postForEntity("/api/menus", request, Menu.class);
        final Menu createdMenu = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/menus/" + createdMenu.getId()),
                () -> assertThat(createdMenu.getName()).isEqualTo("치킨 + 피자"),
                () -> assertThat(createdMenu.getPrice().intValue()).isEqualTo(1000)
        );
    }

    @Test
    void 전체_메뉴_목록을_조회한다() {
        // given
        final Product chicken = createProduct("chicken", 500);
        final Product pizza = createProduct("pizza", 500);
        final MenuGroup menuGroup = createMenuGroup("외식류");

        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(chicken.getId());
        menuProduct1.setQuantity(1);

        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(pizza.getId());
        menuProduct2.setQuantity(1);

        final Menu menu = new Menu();
        menu.setName("치킨 + 피자");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));
        final HttpEntity<Menu> request = new HttpEntity<>(menu);

        testRestTemplate.postForEntity("/api/menus", request, Menu.class);

        // when
        final ResponseEntity<Menu[]> response = testRestTemplate
                .getForEntity("/api/menus", Menu[].class);
        final List<Menu> menus = Arrays.asList(response.getBody());

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(menus).hasSize(1)
        );
    }

    private Product createProduct(final String name, final int price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        final HttpEntity<Product> request = new HttpEntity<>(product);

        return testRestTemplate
                .postForEntity("/api/products", request, Product.class)
                .getBody();
    }

    private MenuGroup createMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup(name);
        final HttpEntity<MenuGroup> request = new HttpEntity<>(menuGroup);

        return testRestTemplate
                .postForEntity("/api/menu-groups", request, MenuGroup.class)
                .getBody();
    }
}
