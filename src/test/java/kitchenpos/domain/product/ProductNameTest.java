package kitchenpos.domain.product;

import kitchenpos.DomainTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DomainTest
class ProductNameTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 255})
    void 상품_이름은_255자_이하이다(int length) {
        final ProductName productName = new ProductName("상".repeat(length));
        assertThat(productName.getName()).isEqualTo("상".repeat(length));
    }

    @Test
    void 상품_이름이_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> new ProductName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_이름이_256자_이상이면_예외가_발생한다() {
        assertThatThrownBy(() -> new ProductName("상".repeat(256)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
