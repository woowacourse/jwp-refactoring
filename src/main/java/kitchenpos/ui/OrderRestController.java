package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.dto.request.ChangeOrderStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest request) {
        OrderResponse response = orderService.create(request);
        URI uri = URI.create("/api/orders/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> list() {
        List<OrderResponse> responses = orderService.list();
        return ResponseEntity.ok()
                .body(responses);
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable final Long orderId,
                                                           @RequestBody final ChangeOrderStatusRequest request) {
        OrderResponse response = orderService.changeOrderStatus(orderId, request);
        return ResponseEntity.ok(response);
    }
}
