package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class ProductRestControllerTest {

    @Autowired
    private ProductRestController productRestController;

    @DisplayName("create 메서드는")
    @Nested
    class create {

        @DisplayName("상품을 등록하고 CREATED를 반환한다")
        @Test
        void addProduct() {
            final var response = productRestController.create(
                    new Product(null, "콜라", BigDecimal.valueOf(1000)));

            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(response.getHeaders().getLocation()).isNotNull()
            );
        }

        @DisplayName("가격이 null인 상품은 등록되지 않고 예외를 던진다")
        @Test
        void priceIsNull_throwsException() {
            assertThatThrownBy(
                    () -> productRestController.create(new Product(null, "콜라", null))
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0 미만 상품은 등록되지 않고 예외를 던진다")
        @Test
        void priceIsUnderZero_throwsException() {
            assertThatThrownBy(
                    () -> productRestController.create(new Product(null, "콜라", BigDecimal.valueOf(-1)))
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class list {

        @DisplayName("상품 목록과 OK를 반환한다")
        @Test
        void allProducts() {
            final var response = productRestController.list();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }
}
