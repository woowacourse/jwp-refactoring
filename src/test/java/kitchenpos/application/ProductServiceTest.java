package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        Product product = productService.create(new Product("제육", BigDecimal.ONE));

        assertThat(product.getId()).isNotNull();
    }

    @DisplayName("상품의 가격이 0이거나 없으면 예외가 발새한다")
    @MethodSource
    @NullSource
    @ParameterizedTest
    void createFailureWhenPriceIsNullOrNegative(BigDecimal price) {
        assertThatThrownBy(() -> productService.create(new Product("제육 볶음", price)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    public static Stream<BigDecimal> createFailureWhenPriceIsNullOrNegative() {
        return Stream.of(BigDecimal.valueOf(-1L), BigDecimal.valueOf(-100L));
    }

    @DisplayName("상품의 목록을 조회한다.")
    @Test
    void list() {
        productService.create(new Product("제육", BigDecimal.ONE));

        assertThat(productService.list()).hasSize(1);
    }
}
