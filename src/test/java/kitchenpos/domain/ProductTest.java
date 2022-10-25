package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @DisplayName("상품 가격은 0 이상이어야 한다.")
    @Test
    void createAndList_invalidPrice() {
        BigDecimal 음수_가격 = BigDecimal.valueOf(-10000);

        assertThatThrownBy(() -> new Product(1L, "후라이드", 음수_가격))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격은 0 이상이어야 한다.");
    }
}
