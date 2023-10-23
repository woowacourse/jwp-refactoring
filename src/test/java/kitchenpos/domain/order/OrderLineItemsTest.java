package kitchenpos.domain.order;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderLineItemsTest {

    @Test
    void 주문_항목이_없는_경우_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> new OrderLineItems(of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목 목록이 있어야 합니다.");
    }
}
