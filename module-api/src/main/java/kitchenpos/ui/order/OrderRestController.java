package kitchenpos.ui.order;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.dto.request.OrderRequestDto;
import kitchenpos.dto.request.OrderStatusRequestDto;
import kitchenpos.dto.response.OrderResponseDto;
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

        final OrderResponseDto responseDto = orderService.create(orderRequestDto);

        return ResponseEntity.created(URI.create("/api/orders/" + responseDto.getId()))
            .body(responseDto)
            ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponseDto>> list() {
        List<OrderResponseDto> responseDtos = orderService.list();

        return ResponseEntity.ok()
            .body(responseDtos)
            ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponseDto> changeOrderStatus(
        @PathVariable final Long orderId,
        @RequestBody final OrderStatusRequestDto orderStatusRequestDto
    ) {
        OrderResponseDto responseDto = orderService
            .changeOrderStatus(orderId, orderStatusRequestDto);
        return ResponseEntity.ok(responseDto);
    }
}
