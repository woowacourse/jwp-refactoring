package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("[예외] 가격이 0보다 작은 메뉴 생성")
    @Test
    void create_Fail_With_InvalidPrice() {
        assertThatThrownBy(
            () -> Menu.builder()
                .price(BigDecimal.valueOf(-1))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }
}