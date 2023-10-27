package com.kitchenpos.ui;

import com.kitchenpos.application.OrderService;
import com.kitchenpos.application.dto.OrderCreateRequest;
import com.kitchenpos.application.dto.OrderUpdateRequest;
import com.kitchenpos.domain.Order;
import com.kitchenpos.ui.dto.OrderResponse;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest orderCreateRequest) {
        Order order = orderService.create(orderCreateRequest);
        URI uri = URI.create("/api/orders/" + order.getId());

        return ResponseEntity.created(uri)
                .body(OrderResponse.from(order));
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        List<OrderResponse> response = orderService.list()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderUpdateRequest orderUpdateRequest
    ) {
        Order result = orderService.changeOrderStatus(orderId, orderUpdateRequest);

        return ResponseEntity.ok(OrderResponse.from(result));
    }
}
