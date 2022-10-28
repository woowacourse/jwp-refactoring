package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 생성_시_주문항목이_빈_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Order(1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문항목이 비어있습니다.");
    }
}
