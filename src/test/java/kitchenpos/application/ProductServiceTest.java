package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ProductService의")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        private static final long PRODUCT_ID = 1L;
        private static final String PRODUCT_NAME = "productA";
        private static final long PRODUCT_PRICE = 1000L;

        private Product product;

        @BeforeEach
        void setUp() {
            product = new Product(PRODUCT_ID, PRODUCT_NAME, BigDecimal.valueOf(PRODUCT_PRICE));
        }

        @Test
        @DisplayName("상품의 이름과 가격을 받으면, 상품을 저장하고 내용을 반환한다.")
        void success() {
            //when
            Product actual = productService.create(product);

            //then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo(PRODUCT_NAME),
                    () -> assertThat(actual.getPrice().compareTo(BigDecimal.valueOf(PRODUCT_PRICE))).isSameAs(0)
            );

        }

        @Test
        @DisplayName("상품의 가격이 없으면, 예외를 던진다.")
        void fail_noPrice() {
            //given
            product = new Product(PRODUCT_ID, PRODUCT_NAME, null);

            //when & then
            Assertions.assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("상품의 가격이 0보다 작으면, 예외를 던진다.")
        void fail_zeroOrNegativePrice() {
            //given
            product = new Product(PRODUCT_ID, PRODUCT_NAME, BigDecimal.valueOf(-1));

            //when & then
            Assertions.assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
