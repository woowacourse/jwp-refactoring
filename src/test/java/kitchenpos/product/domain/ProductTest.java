package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("0 미만의 가격으로 제품을 생성할 수 없다.")
    void NegativePriceError() {
        assertThatThrownBy(() -> new Product("test", BigDecimal.valueOf(-1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격은 음수일 수 없습니다.");
    }
}
