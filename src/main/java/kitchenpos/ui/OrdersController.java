package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrdersService;
import kitchenpos.ui.dto.OrdersAssembler;
import kitchenpos.ui.dto.request.OrdersRequest;
import kitchenpos.ui.dto.request.OrdersStatusRequest;
import kitchenpos.ui.dto.response.OrdersResponse;
import kitchenpos.ui.dto.response.OrdersStatusResponse;
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
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(final OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping
    public ResponseEntity<OrdersResponse> create(@RequestBody OrdersRequest request) {
        OrdersResponse response = OrdersAssembler
            .ordersResponse(ordersService.create(OrdersAssembler.ordersRequestDto(request)));
        URI uri = URI.create("/api/orders/" + response.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrdersResponse>> list() {
        List<OrdersResponse> responses = OrdersAssembler
            .ordersResponses(ordersService.list());

        return ResponseEntity.ok().body(responses);
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<OrdersStatusResponse> changeOrderStatus(
        @PathVariable Long orderId,
        @RequestBody OrdersStatusRequest request
    ) {
        OrdersStatusResponse response = OrdersAssembler
            .ordersStatusResponse(ordersService.changeOrderStatus(
                OrdersAssembler.ordersStatusRequestDto(orderId, request)
            ));

        return ResponseEntity.ok(response);
    }
}
