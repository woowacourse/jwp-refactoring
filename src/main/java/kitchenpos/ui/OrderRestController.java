package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderStatusRequest;
import kitchenpos.ui.dto.OrderStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/order")
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid final OrderRequest orderRequest) {
        final OrderResponse orderResponse = orderService.create(orderRequest);
        final URI uri = URI.create("/api/order/" + orderResponse.getId());
        return ResponseEntity.created(uri)
                .body(orderResponse)
                ;
    }

    @GetMapping("/api/order")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list2())
                ;
    }

    @PutMapping("/api/order/{orderId}/order-status")
    public ResponseEntity<OrderStatusResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusRequest orderStatusRequest
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatusRequest));
    }
}
