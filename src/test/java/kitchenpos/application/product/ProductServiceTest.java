package kitchenpos.application.product;

import kitchenpos.application.ProductService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;


class ProductServiceTest extends ApplicationTestConfig {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("[SUCCESS] 상품을 생성한다.")
    @Test
    void success_create() {
        // given
        final Product expected = new Product("테스트용 상품 이름", new BigDecimal("10000"));

        // when
        final Product actual = productService.create(expected);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isPositive();
            softly.assertThat(actual.getName()).isEqualTo(expected.getName());
            softly.assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
        });
    }

    @DisplayName("[EXCEPTION] 상품의 가격이 음수일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"-1", "-10", "-100000"})
    void throwException_when_create_price_isNegative(final String negativeValue) {
        // given
        final BigDecimal negativeBigDecimal = new BigDecimal(negativeValue);
        final Product expected = new Product("테스트용 상품 이름", negativeBigDecimal);

        // expect
        assertThatThrownBy(() -> productService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[EXCEPTION] 상품의 가격이 null 일 경우 예외가 발생한다.")
    @Test
    void throwException_when_create_price_isNull() {
        // given
        final BigDecimal nullBigDecimal = null;
        final Product expected = new Product("테스트용 상품 이름", nullBigDecimal);

        // expect
        assertThatThrownBy(() -> productService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
