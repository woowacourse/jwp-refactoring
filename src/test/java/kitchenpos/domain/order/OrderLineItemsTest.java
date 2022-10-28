package kitchenpos.domain.order;

import static kitchenpos.fixture.Fixture.ORDER_첫번째_주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    void 생성_시_null이면_값이_존재하지_않는_리스트로_생성한다() {
        final OrderLineItems orderLineItems = new OrderLineItems(null);

        assertThat(orderLineItems.getValue()).isEmpty();
    }

    @Test
    void 생성_시_중복메뉴가_있으면_예외가_발생한다() {
        final OrderLineItem orderLineItem1 = new OrderLineItem(ORDER_첫번째_주문, 1L, 5);
        final OrderLineItem orderLineItem2 = new OrderLineItem(ORDER_첫번째_주문, 2L, 5);
        final OrderLineItem orderLineItem3 = new OrderLineItem(ORDER_첫번째_주문, 1L, 5);

        assertThatThrownBy(() -> new OrderLineItems(List.of(orderLineItem1, orderLineItem2, orderLineItem3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 menu가 존재합니다.");
    }
}
