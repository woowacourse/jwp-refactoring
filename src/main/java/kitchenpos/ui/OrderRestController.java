package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.CreateOrderRequest;
import kitchenpos.ui.dto.PutOrderStatusRequest;
import kitchenpos.ui.response.OrderLineItemDtoInOrderResponse;
import kitchenpos.ui.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final CreateOrderRequest orderRequest) {
        final Order order = orderService.create(orderRequest);
        final OrderResponse response = toResponse(order);
        final URI uri = URI.create("/api/orders/" + order.getId());
        return ResponseEntity.created(uri)
                             .body(response);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final List<OrderResponse> responses = orderService.list().stream()
                                                          .map(this::toResponse)
                                                          .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok()
                             .body(responses);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final PutOrderStatusRequest orderStatusRequest
    ) {
        final Order order = orderService.changeOrderStatus(orderId, orderStatusRequest);
        return ResponseEntity.ok(toResponse(order));
    }

    private OrderResponse toResponse(final Order order) {
        final List<OrderLineItemDtoInOrderResponse> orderLineItems = order.getOrderLineItems()
                                                                          .stream()
                                                                          .map(orderLineItem ->
                                                                                  new OrderLineItemDtoInOrderResponse(
                                                                                          orderLineItem.getMenu()
                                                                                                       .getId()
                                                                                  )
                                                                          ).collect(Collectors.toUnmodifiableList());
        return new OrderResponse(order.getId(), order.getOrderedTime(), order.getOrderStatus(), orderLineItems);
    }
}
