package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.request.OrderChangeRequestDto;
import kitchenpos.application.dto.request.OrderCreateRequestDto;
import kitchenpos.application.dto.response.OrderResponseDto;
import kitchenpos.ui.dto.request.OrderChangeRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.response.OrderResponse;
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

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody OrderCreateRequest orderCreateRequest) {
        OrderCreateRequestDto orderCreateRequestDto = orderCreateRequest.toDto();
        OrderResponseDto orderResponseDto = orderService.create(orderCreateRequestDto);
        OrderResponse orderResponse = OrderResponse.from(orderResponseDto);
        final URI uri = URI.create("/api/orders/" + orderResponse.getId());
        return ResponseEntity.created(uri)
            .body(orderResponse);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        List<OrderResponse> orderResponses = orderService.list()
            .stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok()
            .body(orderResponses);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
        @PathVariable Long orderId,
        @RequestBody OrderChangeRequest orderChangeRequest
    ) {
        OrderChangeRequestDto orderChangeRequestDto =
            new OrderChangeRequestDto(orderId, orderChangeRequest.getOrderStatus());
        OrderResponseDto orderResponseDto = orderService.changeOrderStatus(orderChangeRequestDto);
        OrderResponse orderResponse = OrderResponse.from(orderResponseDto);
        return ResponseEntity.ok(orderResponse);
    }
}
