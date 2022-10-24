package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품 저장")
    @Test
    void create() {
        final var product = new Product("콜라", new BigDecimal(1000));
        final var result = productService.create(product);

        assertThat(product).isEqualTo(result);
    }

    @DisplayName("상품 저장 시 가격이 null이라면 예외 발생")
    @Test
    void create_priceIsNull_throwsException() {
        final var invalidProduct = new Product("사이다", null);

        assertThatThrownBy(
                () -> productService.create(invalidProduct)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 저장 시 가격이 0 미만이라면 예외 발생")
    @Test
    void create_priceIsUnderZero_throwsException() {
        final var invalidProduct = new Product("환타", new BigDecimal(-1));

        assertThatThrownBy(
                () -> productService.create(invalidProduct)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 상품 목록 조회")
    @Test
    void list() {
        final var existingProducts = productService.list();

        final var coke = new Product("콜라", new BigDecimal(1000));
        final var rice = new Product("공기밥", new BigDecimal(1500));

        productService.create(coke);
        productService.create(rice);

        final var result = productService.list();
        final var filteredResult = result.stream()
                .filter(product -> !existingProducts.contains(product))
                .collect(Collectors.toList());

        assertThat(filteredResult).containsExactly(coke, rice);
    }
}
