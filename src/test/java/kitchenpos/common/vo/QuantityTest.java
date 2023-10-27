package kitchenpos.common.vo;

import kitchenpos.common.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class QuantityTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        assertThatCode(() -> new Quantity(10))
                .doesNotThrowAnyException();
    }
}
