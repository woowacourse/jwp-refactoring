package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.service.OrderDto;
import kitchenpos.order.service.OrderLineItemDto;

public enum OrderFixture {

    ORDER_1(1L, 1L, "COOKING", null, List.of(OrderLineItemFixture.ORDER_LINE_ITEM_1.toDto())),
    ;

    public final Long id;
    public final Long orderTableId;
    public final String orderStatus;
    public final String orderedTime;
    public final List<OrderLineItemDto> orderLineItemDtos;

    OrderFixture(
        Long id,
        Long orderTableId,
        String orderStatus,
        String orderedTime,
        List<OrderLineItemDto> orderLineItemDtos
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public static OrderDto computeDefaultOrderDto(Consumer<OrderDto> consumer) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setOrderTableId(1L);
        orderDto.setOrderStatus("COOKING");
        orderDto.setOrderedTime(null);
        orderDto.setOrderLineItemDtos(List.of(OrderLineItemFixture.ORDER_LINE_ITEM_1.toDto()));
        consumer.accept(orderDto);
        return orderDto;
    }

    public OrderDto toDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);
        orderDto.setOrderTableId(orderTableId);
        orderDto.setOrderStatus(orderStatus);
        orderDto.setOrderedTime(null);
        orderDto.setOrderLineItemDtos(orderLineItemDtos);
        return orderDto;
    }

    public Order toEntity() {
        return new Order.Builder()
            .orderTable(OrderTableFixture.OCCUPIED_TABLE.toEntity())
            .orderLineItems(List.of(OrderLineItemFixture.ORDER_LINE_ITEM_1.toEntity()))
            .orderStatus(OrderStatus.COOKING)
            .orderedTime(LocalDateTime.now())
            .build();
    }

    public enum OrderLineItemFixture {

        ORDER_LINE_ITEM_1(1L, 1L, 1L, 1L),
        ;

        private final Long seq;
        private final Long orderId;
        private final Long menuId;
        private final Long quantity;

        OrderLineItemFixture(Long seq, Long orderId, Long menuId, Long quantity) {
            this.seq = seq;
            this.orderId = orderId;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public OrderLineItemDto toDto() {
            OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
            orderLineItemDto.setSeq(seq);
            orderLineItemDto.setOrderId(orderId);
            orderLineItemDto.setMenuId(menuId);
            orderLineItemDto.setQuantity(quantity);
            return orderLineItemDto;
        }

        public OrderLineItem toEntity() {
            return new OrderLineItem.Builder()
                .menu(MenuFixture.LUNCH_SPECIAL.toEntity())
                .quantity(quantity)
                .build();
        }
    }
}
