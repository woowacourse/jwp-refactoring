package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("create 메서드는")
    @Nested
    class create {

        @DisplayName("상품을 저장하고, 저장된 상품을 반환한다")
        @Test
        void saveProduct() {
            final var product = new Product("콜라", new BigDecimal(1000));
            final var result = productService.create(product);

            assertThat(product).isEqualTo(result);
        }

        @DisplayName("상품 가격이 null 이라면")
        @Nested
        class priceIsNull {

            private final BigDecimal invalidPrice = null;

            @DisplayName("예외를 던진다")
            @Test
            void throwsException() {
                final var invalidPriceProduct = new Product("사이다", invalidPrice);
                assertThatThrownBy(
                        () -> productService.create(invalidPriceProduct)
                ).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @DisplayName("상품 가격이 null 이라면")
        @Nested
        class priceIsUnderZero {

            private final BigDecimal invalidPrice = new BigDecimal(-1);

            @DisplayName("예외를 던진다")
            @Test
            void throwsException() {
                final var invalidPriceProduct = new Product("환타", invalidPrice);
                assertThatThrownBy(
                        () -> productService.create(invalidPriceProduct)
                ).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class list {

        @DisplayName("등록된 모든 상품 목록을 조회해 반환한다")
        @Test
        void findAllProducts() {
            final var coke = new Product("콜라", new BigDecimal(1000));
            final var rice = new Product("공기밥", new BigDecimal(1500));

            productService.create(coke);
            productService.create(rice);

            final var result = productService.list();
            assertThat(result).contains(coke, rice);
        }
    }
}
