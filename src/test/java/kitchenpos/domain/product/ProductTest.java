package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 상품의_가격이_null이면_예외가_발생한다() {
        assertThatThrownBy(
                () -> new Product("라면", null)
        );
    }

    @Test
    void 상품_가격이_0원보다_작으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> new Product("라면", new BigDecimal( -1))
        );
    }
}
