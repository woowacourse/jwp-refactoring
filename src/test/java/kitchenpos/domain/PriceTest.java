package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PriceTest {

    @DisplayName("가격이 0보다 작은 경우 예외를 반환한다.")
    @Test
    void validateUnderZero() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 공백이거나 0원보다 작을 수 없습니다.");
    }
}
