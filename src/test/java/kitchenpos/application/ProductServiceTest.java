package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        // given
        final Product product = ProductFixture.createDefaultWithoutId();

        // when
        final Product saved = productService.create(product);

        // then
        assertThat(saved).usingRecursiveComparison()
                .ignoringFields("id")
                .withComparatorForFields((Comparator<BigDecimal>) BigDecimal::compareTo, "price")
                .isEqualTo(product);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1})
    @DisplayName("제품 가격이 Null이거나 음수인 경우 예외가 발생한다.")
    void create_exceptionWhenPriceIsNullZero(final Long price) {
        // given
        final Product product = ProductFixture.createWithPrice(price);

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록된 제품을 조회한다.")
    void list() {
        // given
        final Product product1 = ProductFixture.createDefaultWithoutId();
        final Product product2 = ProductFixture.createDefaultWithoutId();

        productService.create(product1);
        productService.create(product2);

        // when
        final List<Product> actual = productService.list();

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .extracting("id")
                .containsExactly(1L, 2L);
    }
}
