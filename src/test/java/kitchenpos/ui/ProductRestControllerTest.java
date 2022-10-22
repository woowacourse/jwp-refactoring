package kitchenpos.ui;

import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ProductRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("Product를 생성한다.")
    void create() {
        ResponseEntity<Product> response = post(url("/api/products"), new Product(PRODUCT1_NAME, PRODUCT1_PRICE),
                Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("모든 Product를 조회한다.")
    void list() {
        ResponseEntity<Product[]> response = get(url("/api/products"), Product[].class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotEmpty()
        );
    }
}
