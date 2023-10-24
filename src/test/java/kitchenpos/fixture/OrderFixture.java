package kitchenpos.fixture;

import java.util.List;
import java.util.function.Consumer;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemDto;

public enum OrderFixture {

    ORDER_1(1L, 1L, "COOKING", null, List.of(OrderLineItemFixture.ORDER_LINE_ITEM_1.toDto())),
    ;

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final String orderedTime;
    private final List<OrderLineItemDto> orderLineItemDtos;

    OrderFixture(Long id, Long orderTableId, String orderStatus, String orderedTime,
        List<OrderLineItemDto> orderLineItemDtos) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public static OrderDto computeDefaultOrder(Consumer<OrderDto> consumer) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setOrderTableId(1L);
        orderDto.setOrderStatus("COOKING");
        orderDto.setOrderedTime(null);
        orderDto.setOrderLineItemDtos(List.of(OrderLineItemFixture.ORDER_LINE_ITEM_1.toDto()));
        consumer.accept(orderDto);
        return orderDto;
    }

    public OrderDto toEntity() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);
        orderDto.setOrderTableId(orderTableId);
        orderDto.setOrderStatus(orderStatus);
        orderDto.setOrderedTime(null);
        orderDto.setOrderLineItemDtos(orderLineItemDtos);
        return orderDto;
    }

    private enum OrderLineItemFixture {

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

        private OrderLineItemDto toDto() {
            OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
            orderLineItemDto.setSeq(seq);
            orderLineItemDto.setOrderId(orderId);
            orderLineItemDto.setMenuId(menuId);
            orderLineItemDto.setQuantity(quantity);
            return orderLineItemDto;
        }
    }
}
