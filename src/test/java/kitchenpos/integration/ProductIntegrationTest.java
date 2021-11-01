package kitchenpos.integration;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.testutils.TestDomainBuilder.productBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductIntegrationTest extends AbstractIntegrationTest {

    @DisplayName("POST /api/products - (이름, 가격)으로 상품을 추가한다.")
    @Test
    void create() {
        // given
        String name = "깐부치킨";
        BigDecimal price = BigDecimal.valueOf(20000);
        Product newProduct = productBuilder()
                .name(name)
                .price(price)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // when
        ResponseEntity<Product> responseEntity = post(
                "/api/products",
                httpHeaders,
                newProduct,
                new ParameterizedTypeReference<Product>() {
                }
        );
        Product createdProduct = responseEntity.getBody();

        // then
        assertThat(createdProduct).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(extractLocation(responseEntity)).isEqualTo("/api/products/" + createdProduct.getId());
        assertThat(createdProduct.getId()).isNotNull();
        assertThat(createdProduct.getName()).isEqualTo(name);
        assertThat(createdProduct.getPrice()).isEqualByComparingTo(price);
    }

    @DisplayName("GET /api/products - 상품의 리스트를 가져온다. (list)")
    @Test
    void list() {
        // when
        ResponseEntity<List<Product>> responseEntity = get(
                "/api/products",
                new ParameterizedTypeReference<List<Product>>() {
                }
        );
        List<Product> products = responseEntity.getBody();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(products).hasSize(6);
    }
}
