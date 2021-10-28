package kitchenpos.integration;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class MenuIntegrationTest extends IntegrationTest {

    private static final String MENU_URL = "/api/menus";

    private Long menuGroupId;

    private String menuName;

    private BigDecimal menuPrice;

    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(new BigDecimal(17000));
        Product savedProduct = productDao.save(product);
        Long productId = savedProduct.getId();

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        menuGroupId = savedMenuGroup.getId();

        menuName = "후라이드+후라이드";
        menuPrice = new BigDecimal(19000);

        menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(2L);
    }

    @DisplayName("menu 를 생성한다")
    @Test
    void create() {
        // given
        Menu menu = new Menu();
        menu.setName(menuName);
        menu.setPrice(menuPrice);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(
                Collections.singletonList(menuProduct)
        );

        // when
        ResponseEntity<Menu> menuResponseEntity = testRestTemplate.postForEntity(
                MENU_URL,
                menu,
                Menu.class
        );
        HttpStatus statusCode = menuResponseEntity.getStatusCode();
        URI location = menuResponseEntity.getHeaders().getLocation();
        Menu body = menuResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(location).isEqualTo(URI.create(MENU_URL + "/1"));

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getName()).isEqualTo(menuName);
        assertThat(body.getPrice())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(menuPrice);
        assertThat(body.getMenuProducts().get(0))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(menuProduct);
    }

    @DisplayName("전체 menu 를 조회한다")
    @Test
    void list() {
        // given
        Menu menu = new Menu();
        menu.setName(menuName);
        menu.setPrice(menuPrice);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(
                Collections.singletonList(menuProduct)
        );
        menuDao.save(menu);

        // when
        ResponseEntity<Menu[]> menuResponseEntity = testRestTemplate.getForEntity(
                MENU_URL,
                Menu[].class
        );
        HttpStatus statusCode = menuResponseEntity.getStatusCode();
        Menu[] body = menuResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);

        assertThat(body)
                .hasSize(1)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        menuName
                );
    }
}
