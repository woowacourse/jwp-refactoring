package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void createProduct() {
        // given
        final Product product = ProductFixture.createDefaultWithoutId();

        // when
        final ResponseEntity<Product> response =
                testRestTemplate.postForEntity("http://localhost:" + localServerPort + "/api/products", product,
                        Product.class);

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getBody()).usingRecursiveComparison()
                        .ignoringFields("id")
                        .withComparatorForFields((Comparator<BigDecimal>) BigDecimal::compareTo, "price")
                        .isEqualTo(product)
        );
    }

    @Test
    @DisplayName("상품을 조회할 수 있다.")
    void listProducts() {
        // given
        final Product product = ProductFixture.createDefaultWithoutId();
        testRestTemplate.postForEntity("http://localhost:" + localServerPort + "/api/products", product,
                Product.class);

        // when
        final ResponseEntity<List> response = testRestTemplate.getForEntity(
                "http://localhost:" + localServerPort + "/api/products", List.class);

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody())
                        .extracting("id", Long.class)
                        .containsExactly(1)
        );
    }
}
