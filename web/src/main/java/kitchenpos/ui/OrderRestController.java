package kitchenpos.ui;

import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.service.OrderService;
import kitchenpos.service.OrderMapper;
import kitchenpos.service.OrderCrudService;
import kitchenpos.service.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {

    private final OrderService orderService;
    private final OrderCrudService orderCrudService;
    private final OrderMapper orderMapper;

    public OrderRestController(
        final OrderService orderService,
        final OrderCrudService orderCrudService,
        final OrderMapper orderMapper
    ) {
        this.orderService = orderService;
        this.orderCrudService = orderCrudService;
        this.orderMapper = orderMapper;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderDto> create(@RequestBody final OrderDto orderDto) {
        final Order created = orderCrudService.create(orderMapper.toEntity(orderDto));
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(OrderDto.from(created))
            ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderDto>> list() {
        List<OrderDto> list = orderCrudService.list()
                                              .stream()
                                              .map(OrderDto::from)
                                              .collect(Collectors.toList());
        return ResponseEntity.ok()
                             .body(list)
            ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderDto> changeOrderStatus(
        @PathVariable final Long orderId,
        @RequestBody final OrderDto orderDto
    ) {
        Order order = orderService.changeOrderStatus(orderId, orderMapper.toEntity(orderDto));
        return ResponseEntity.ok(OrderDto.from(order));
    }
}
