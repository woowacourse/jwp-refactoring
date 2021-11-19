package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.ui.dto.request.order.OrderLineItemDto;
import kitchenpos.ui.dto.request.order.OrderRequestDto;
import kitchenpos.ui.dto.response.order.OrderLineItemResponseDto;
import kitchenpos.ui.dto.response.order.OrderResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponseDto> create(
        @RequestBody final OrderRequestDto orderRequestDto) {

        final Order created = orderService.create(new Order(
            orderRequestDto.getOrderTableId(),
            toOrderLineItems(orderRequestDto.getOrderLineItems())
        ));

        final OrderResponseDto responseDto = toOrderResponseDto(created);
        return ResponseEntity.created(URI.create("/api/orders/" + created.getId()))
            .body(responseDto)
            ;
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemDto> orderLineItems) {
        return orderLineItems.stream()
            .map(dto -> new OrderLineItem(dto.getMenuId(), dto.getMenuId()))
            .collect(toList());
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponseDto>> list() {
        List<Order> orders = orderService.list();
        List<OrderResponseDto> responseDtos = orders.stream()
            .map(this::toOrderResponseDto)
            .collect(toList());

        return ResponseEntity.ok()
            .body(responseDtos)
            ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponseDto> changeOrderStatus(
        @PathVariable final Long orderId,
        @RequestBody final OrderRequestDto orderRequestDto
    ) {
        Order created = orderService.changeOrderStatus(orderId, new Order(
            orderRequestDto.getOrderTableId(),
            toOrderLineItems(orderRequestDto.getOrderLineItems())
        ));

        OrderResponseDto responseDto = toOrderResponseDto(created);
        return ResponseEntity.ok(responseDto);
    }

    private OrderResponseDto toOrderResponseDto(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getOrderTableId(),
            order.getOrderStatus(),
            order.getOrderedTime(),
            toOrderLineItemsResponseDto(order.getOrderLineItems()));
    }

    private List<OrderLineItemResponseDto> toOrderLineItemsResponseDto(
        List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(orderLineItem ->
                new OrderLineItemResponseDto(
                    orderLineItem.getSeq(),
                    orderLineItem.getOrderId(),
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity())
            ).collect(toList());
    }
}
