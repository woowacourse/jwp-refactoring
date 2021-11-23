package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품 생성 성공")
    @Test
    void create() {
        assertThatCode(() -> new Product("강정치킨", BigDecimal.valueOf(17000)))
                .doesNotThrowAnyException();
    }

    @DisplayName("상품 생성 실패 - 가격 부재")
    @Test
    void createByNullPrice() {
        assertThatThrownBy(() -> new Product("강정치킨", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 실패 - 가격 0 미만")
    @Test
    void createByNegativePrice() {
        assertThatThrownBy(() -> new Product("강정치킨", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
