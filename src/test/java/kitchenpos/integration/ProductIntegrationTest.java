package kitchenpos.integration;

import static kitchenpos.integration.templates.ProductTemplate.PRODUCT_URL;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URI;
import kitchenpos.domain.Product;
import kitchenpos.integration.annotation.IntegrationTest;
import kitchenpos.integration.templates.ProductTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@IntegrationTest
class ProductIntegrationTest {

    @Autowired
    private ProductTemplate productTemplate;

    private String productName;

    private BigDecimal productPrice;

    @BeforeEach
    void setUp() {
        productName = "강정치킨";
        productPrice = new BigDecimal(17000);
    }

    @DisplayName("product 를 생성한다")
    @Test
    void create() {
        // given // when
        ResponseEntity<Product> productResponseEntity = productTemplate
            .create(
                productName,
                productPrice
            );
        HttpStatus statusCode = productResponseEntity.getStatusCode();
        URI location = productResponseEntity.getHeaders().getLocation();
        Product body = productResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getName()).isEqualTo(productName);
        assertThat(body.getPrice())
            .usingComparator(BigDecimal::compareTo)
            .isEqualTo(productPrice);
        assertThat(location).isEqualTo(URI.create(PRODUCT_URL + "/" + body.getId()));
    }

    @DisplayName("전체 product 를 조회한다")
    @Test
    void list() {
        // given
        productTemplate
            .create(
                productName,
                productPrice
            );

        String secondProductName = "매콤치킨";
        BigDecimal secondProductPrice = new BigDecimal(18000);
        productTemplate
            .create(
                secondProductName,
                secondProductPrice
            );

        // when
        ResponseEntity<Product[]> productResponseEntity = productTemplate.list();
        HttpStatus statusCode = productResponseEntity.getStatusCode();
        Product[] body = productResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body)
            .hasSize(2)
            .extracting("name")
            .containsExactlyInAnyOrder(
                productName, secondProductName
            );
    }
}
