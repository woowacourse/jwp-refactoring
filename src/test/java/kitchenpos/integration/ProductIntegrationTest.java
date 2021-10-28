package kitchenpos.integration;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class ProductIntegrationTest extends IntegrationTest {

    private static final String PRODUCT_URL = "/api/products";

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
        // given
        Product product = new Product();
        product.setName(productName);
        product.setPrice(productPrice);

        // when
        ResponseEntity<Product> productResponseEntity = testRestTemplate.postForEntity(
                PRODUCT_URL,
                product,
                Product.class
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
        Product product = new Product();
        product.setName(productName);
        product.setPrice(productPrice);
        productDao.save(product);

        String secondProductName = "매콤치킨";
        BigDecimal secondProductPrice = new BigDecimal(18000);
        Product secondProduct = new Product();
        secondProduct.setName(secondProductName);
        secondProduct.setPrice(secondProductPrice);
        productDao.save(secondProduct);

        // when
        ResponseEntity<Product[]> productResponseEntity = testRestTemplate.getForEntity(
                PRODUCT_URL,
                Product[].class
        );
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
