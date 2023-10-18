package kitchenpos.ui;

import kitchenpos.application.OrderTableService;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderTableRestController {

    private final OrderTableService orderTableService;

    public OrderTableRestController(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping("/api/order-tables/{tableGroupId}")
    public ResponseEntity<Long> create(@PathVariable Long tableGroupId, @RequestParam int numberOfGuests) {
        Long orderTableId = orderTableService.create(tableGroupId, numberOfGuests);
        final URI uri = URI.create("/api/orders/" + orderTableId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/api/order-tables")
    public ResponseEntity<List<OrderTableResponse>> findAll() {
        List<OrderTableResponse> responses = orderTableService.findAll();
        return ResponseEntity.ok().body(responses);
    }
}
