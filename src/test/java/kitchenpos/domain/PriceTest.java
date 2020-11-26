package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PriceTest {

    @DisplayName("가격 생성")
    @Test
    void create() {
        assertThat(Price.of(new BigDecimal(18_000)))
            .isInstanceOf(Price.class);
    }

    @DisplayName("새로운 가격을 생성한다. - 메뉴 가격이 null일 경우")
    @Test
    void create_IfPriceIsNull_ThrowException() {
        assertThatThrownBy(() -> Price.of(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격은 null일 수 없습니다.");
    }

    @DisplayName("새로운 상품을 생성한다. - 메뉴 가격이 0 이하일 경우")
    @Test
    void createIfPriceIsNotPositive_ThrowException() {
        assertThatThrownBy(() -> Price.of(new BigDecimal(-18_000L)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격은 0 이상이어야 합니다.");
    }
}
