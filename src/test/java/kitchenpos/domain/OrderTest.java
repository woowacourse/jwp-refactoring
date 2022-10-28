package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 생성 시 주문 항목이 비어있으면 예외가 발생한다.")
    @Test
    void createWithEmptyOrderLineItem() {
        assertThatThrownBy(() -> new Order(1L, "COOKING", LocalDateTime.now(), new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
