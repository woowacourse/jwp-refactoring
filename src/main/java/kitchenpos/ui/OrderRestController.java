package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.OrderChangeRequest;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderRestController {
    private final OrderService orderService;

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody final OrderCreateRequest orderCreateRequest) {
        final Order created = orderService.create(orderCreateRequest.toRequestEntity());
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
            .body(OrderResponse.from(created));
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final List<OrderResponse> orderResponses = orderService.list()
            .stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok()
            .body(orderResponses);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
        @PathVariable final Long orderId,
        @Valid @RequestBody final OrderChangeRequest orderChangeRequest
    ) {
        return ResponseEntity.ok(
            OrderResponse.from(orderService.changeOrderStatus(orderId, orderChangeRequest.toRequestEntity())));
    }
}
