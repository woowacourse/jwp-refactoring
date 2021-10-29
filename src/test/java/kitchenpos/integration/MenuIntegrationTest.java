package kitchenpos.integration;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.integration.annotation.IntegrationTest;
import kitchenpos.integration.templates.MenuGroupTemplate;
import kitchenpos.integration.templates.MenuTemplate;
import kitchenpos.integration.templates.ProductTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;

import static kitchenpos.integration.templates.MenuTemplate.MENU_URL;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class MenuIntegrationTest {

    @Autowired
    private MenuTemplate menuTemplate;

    @Autowired
    private ProductTemplate productTemplate;

    @Autowired
    private MenuGroupTemplate menuGroupTemplate;

    private Long menuGroupId;

    private String menuName;

    private BigDecimal menuPrice;

    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        Product product = productTemplate
                .create(
                        "강정치킨",
                        new BigDecimal(17000)
                )
                .getBody();
        assertThat(product).isNotNull();
        Long productId = product.getId();

        MenuGroup menuGroup = menuGroupTemplate
                .create("추천메뉴")
                .getBody();
        assertThat(menuGroup).isNotNull();
        menuGroup.setName("추천메뉴");
        assertThat(menuGroup).isNotNull();
        menuGroupId = menuGroup.getId();

        menuName = "후라이드+후라이드";
        menuPrice = new BigDecimal(19000);

        menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(2L);
    }

    @DisplayName("menu 를 생성한다")
    @Test
    void create() {
        // given // when
        ResponseEntity<Menu> menuResponseEntity = menuTemplate
                .create(
                        menuName,
                        menuPrice,
                        menuGroupId,
                        Collections.singletonList(menuProduct)
                );
        HttpStatus statusCode = menuResponseEntity.getStatusCode();
        URI location = menuResponseEntity.getHeaders().getLocation();
        Menu body = menuResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getName()).isEqualTo(menuName);
        assertThat(body.getPrice())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(menuPrice);
        assertThat(body.getMenuProducts().get(0))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(menuProduct);
        assertThat(location).isEqualTo(URI.create(MENU_URL + "/1"));
    }

    @DisplayName("전체 menu 를 조회한다")
    @Test
    void list() {
        // given
        menuTemplate
                .create(
                        menuName,
                        menuPrice,
                        menuGroupId,
                        Collections.singletonList(menuProduct)
                );

        // when
        ResponseEntity<Menu[]> menuResponseEntity = menuTemplate.list();
        HttpStatus statusCode = menuResponseEntity.getStatusCode();
        Menu[] body = menuResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body)
                .hasSize(1)
                .extracting("name")
                .contains(menuName);
    }
}
