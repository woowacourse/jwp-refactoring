package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    @DisplayName("가격이 null 이거나 0미만인 상품은 등록할 수 없다.")
    @ParameterizedTest(name = "가격이 {0} 인 상품은 등록할 수 없다.")
    @NullSource
    @ValueSource(strings = {"-1"})
    void construct_Exception_Price(BigDecimal price) {
        assertThatThrownBy(() -> new Product("pizza", price))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
