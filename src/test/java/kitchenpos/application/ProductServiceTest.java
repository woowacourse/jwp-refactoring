package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest{

    @Autowired
    private ProductService productService;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("강정치킨", BigDecimal.valueOf(17000));
        product.setId(1L);
    }

    @Test
    @DisplayName("싱품의 가격이 0일 경우 상품이 생성되어야 한다.")
    void createWithValidPrice() {
        // given
        final Product requestProduct = new Product("강정치킨", BigDecimal.valueOf(0));
        product.setPrice(BigDecimal.valueOf(0));

        // when
        when(productDao.save(any())).thenReturn(product);

        // then
        assertDoesNotThrow(() -> productService.create(requestProduct));
    }

    @ParameterizedTest(name = "상품의 가격이 {1}일 경우 예외가 발생한다.")
    @MethodSource("invalidParams")
    void createWithInvalidPrice(final Product product, final String testName) {
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidParams() {
        return Stream.of(
                Arguments.of(new Product("강정치킨", BigDecimal.valueOf(-100)), "-100"),
                Arguments.of(new Product("강정치킨", BigDecimal.valueOf(-1)), "-1"),
                Arguments.of(new Product("강정치킨", null), "null")
        );
    }
}
