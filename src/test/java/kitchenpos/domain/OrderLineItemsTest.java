package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    void orderLineItem이_null이면_OrderLineItems_객체를_생성할_수_없다() {
        // given
        List<OrderLineItem> orderLineItems = null;

        // when & then
        assertThatThrownBy(() -> new OrderLineItems(orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void orderLineItem이_빈_리스트면_OrderLineItems_객체를_생성할_수_없다() {
        // given
        List<OrderLineItem> orderLineItems = Collections.emptyList();

        // when & then
        assertThatThrownBy(() -> new OrderLineItems(orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderLineItems에_같은_메뉴를_가진_orderLineItem이_존재하는지_확인한다() {
        // given
        final OrderLineItems orderLineItems = new OrderLineItems(
                Arrays.asList(
                        new OrderLineItem(null, 1L, "1", BigDecimal.valueOf(1), 1L),
                        new OrderLineItem(null, 1L, "1", BigDecimal.valueOf(1), 1L)
                )
        );

        // when
        final boolean actual = orderLineItems.hasSameMenu();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void orderLineItems에_같은_메뉴를_가진_orderLineItem이_존재하지_않는지_확인한다() {
        // given
        final OrderLineItems orderLineItems = new OrderLineItems(
                Arrays.asList(
                        new OrderLineItem(null, 1L, "1", BigDecimal.valueOf(1), 1L),
                        new OrderLineItem(null, 2L, "2", BigDecimal.valueOf(2), 2L)
                )
        );

        // when
        final boolean actual = orderLineItems.hasSameMenu();

        // then
        assertThat(actual).isFalse();
    }
}
