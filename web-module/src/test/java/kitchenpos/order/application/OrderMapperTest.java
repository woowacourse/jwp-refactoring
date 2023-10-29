 package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderMapperTest {

    @DisplayName("[SUCCESS] OrderSheet 를 OrderMapper 를 통해 Order 로 변환한다.")
    @Test
    void success_map() {
        // given
        final OrderSheet orderSheet = new OrderSheet(
                1L,
                List.of(
                        new OrderSheet.OrderSheetItem(1L, 10L),
                        new OrderSheet.OrderSheetItem(2L, 10L)
                )
        );

        // when
        final OrderMapper orderMapper = new OrderMapper();
        final Order actual = orderMapper.map(orderSheet);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isNull();
            softly.assertThat(actual.getOrderTableId()).isEqualTo(1L);
            softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            softly.assertThat(actual.getOrderedTime()).isBefore(LocalDateTime.now());

            softly.assertThat(actual.getOrderLineItems().getOrderLineItems()).hasSize(2);
            final List<OrderLineItem> actualOrderLineItems = actual.getOrderLineItems().getOrderLineItems();
            final OrderLineItem actualOrderLineItemOne = actualOrderLineItems.get(0);
            final OrderLineItem actualOrderLineItemTwo = actualOrderLineItems.get(1);

            softly.assertThat(actualOrderLineItemOne.getSeq()).isNull();
            softly.assertThat(actualOrderLineItemOne.getMenuId()).isEqualTo(1L);
            softly.assertThat(actualOrderLineItemOne.getQuantity().getValue()).isEqualTo(10L);
            softly.assertThat(actualOrderLineItemTwo.getSeq()).isNull();
            softly.assertThat(actualOrderLineItemTwo.getMenuId()).isEqualTo(2L);
            softly.assertThat(actualOrderLineItemTwo.getQuantity().getValue()).isEqualTo(10L);
        });
    }
}
