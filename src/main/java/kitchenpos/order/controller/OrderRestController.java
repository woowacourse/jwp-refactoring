package kitchenpos.order.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderEditRequest;
import kitchenpos.order.dto.OrderResponses;
import kitchenpos.order.service.OrderService;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<Void> create(@RequestBody @Valid OrderCreateRequest request) {
        final Long id = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + id);
        return ResponseEntity.created(uri)
            .build();
    }

    @GetMapping("/api/orders")
    public ResponseEntity<OrderResponses> list() {
        return ResponseEntity.ok()
            .body(orderService.list())
            ;
    }

    @PutMapping("/api/orders/{orderId}")
    public ResponseEntity<Void> edit(
        @PathVariable Long orderId,
        @RequestBody @Valid OrderEditRequest request
    ) {
        orderService.changeOrderStatus(orderId, request);
        return ResponseEntity.noContent()
            .build();
    }
}
