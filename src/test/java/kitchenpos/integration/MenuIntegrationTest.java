package kitchenpos.integration;

import static kitchenpos.integration.templates.MenuTemplate.MENU_URL;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuResponse;
import kitchenpos.factory.MenuProductFactory;
import kitchenpos.integration.annotation.IntegrationTest;
import kitchenpos.integration.templates.MenuGroupTemplate;
import kitchenpos.integration.templates.MenuTemplate;
import kitchenpos.integration.templates.ProductTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    private MenuProducts menuProducts;

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

        menuGroupId = menuGroup.getId();

        menuName = "후라이드+후라이드";

        menuPrice = new BigDecimal(19000);

        menuProduct = MenuProductFactory.builder()
            .productId(productId)
            .quantity(2L)
            .build();

        menuProducts = new MenuProducts(Collections.singletonList(menuProduct));
    }

    @DisplayName("menu 를 생성한다")
    @Test
    void create() {
        // given // when
        ResponseEntity<MenuResponse> menuResponseEntity = menuTemplate
            .create(
                menuName,
                menuPrice,
                menuGroupId,
                menuProducts
            );
        HttpStatus statusCode = menuResponseEntity.getStatusCode();
        URI location = menuResponseEntity.getHeaders().getLocation();
        MenuResponse body = menuResponseEntity.getBody();

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
                menuProducts
            );

        // when
        ResponseEntity<MenuResponse[]> menuResponseEntity = menuTemplate.list();
        HttpStatus statusCode = menuResponseEntity.getStatusCode();
        MenuResponse[] body = menuResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body)
            .hasSize(1)
            .extracting("name")
            .contains(menuName);
    }
}
