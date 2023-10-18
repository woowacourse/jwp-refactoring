package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {


    @Test
    @DisplayName("상품 등록 시 가격 정보가 있어야 한다.")
    void 상품_등록_실패_가격_없음() {
        // given
        BigDecimal nullPrice = null;

        // expected
        assertThatThrownBy(() -> new Product("새우탕면", nullPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
