package kitchenpos;

import kitchenpos.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void 상품의_가격은_0_이상이어야_한다() {
        //given
        BigDecimal price = BigDecimal.valueOf(-1L);

        //when,then
        assertThatThrownBy(() -> new Product("name", price))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 가격은 0원 이상이어야 합니다.");
    }
}
