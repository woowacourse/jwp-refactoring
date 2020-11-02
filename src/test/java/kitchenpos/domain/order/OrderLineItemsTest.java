package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemsTest {

    @DisplayName("OrderLineItem은 원소가 1개이상 이여야 한다.")
    @Test
    void notEmptyTest() {
        assertThatThrownBy(() -> new OrderLineItems(Collections.emptyList(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderLineItem의 메뉴은 중복될 수 없다.")
    @Test
    void notDuplicateTest() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(1L),
                new OrderLineItem(1L)
        );
        assertThatThrownBy(() -> new OrderLineItems(orderLineItems, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderLineItem은 지정된 Menu를 가진다")
    @Test
    void menuTest() {
        Order order = new Order();

        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(new OrderLineItem(1L)), order);

        OrderLineItem orderLineItem = orderLineItems.getOrderLineItems().get(0);
        assertThat(orderLineItem.getOrder()).isEqualTo(order);
    }
}