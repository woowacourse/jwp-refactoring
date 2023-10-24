package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    @DisplayName("주문할 항목의  최소 개수는 1개이다.")
    void 주문_항목_생성_실패_최소_개수() {
        assertThatThrownBy(() -> new OrderLineItems(Collections.emptyList()))
                .isInstanceOf(Exception.class);
    }
}
