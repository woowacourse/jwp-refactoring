package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("")
    @Test
    void menuCreate() {
        assertThatThrownBy(() -> {
            Menu menu =
                new Menu("후라이드치킨", BigDecimal.valueOf(-16000.00), Fixtures.makeMenuGroup());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
