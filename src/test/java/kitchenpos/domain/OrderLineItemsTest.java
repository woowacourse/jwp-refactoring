package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @DisplayName("주문 목록은 1개 이상 존재해야 한다.")
    @Test
    void createFailTest_ByOrderLineItemIsEmpty() {
        assertThatThrownBy(() -> OrderLineItems.from(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 메뉴는 1개 이상 존재해야 합니다.");
    }

}
