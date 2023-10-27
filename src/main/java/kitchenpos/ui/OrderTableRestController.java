package kitchenpos.ui;

import kitchenpos.application.OrderTableService;
import kitchenpos.dto.request.OrderTableCreateRequest;
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

    @PostMapping("/api/tables")
    public ResponseEntity<Long> create(@RequestBody final OrderTableCreateRequest request) {
        Long orderTableId = orderTableService.create(request.getNumberOfGuests(), request.isEmpty());
        final URI uri = URI.create("/api/orders/" + orderTableId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> findAll() {
        List<OrderTableResponse> responses = orderTableService.findAll();
        return ResponseEntity.ok().body(responses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<Void> updateIsEmpty(
            @PathVariable final Long orderTableId,
            @RequestParam final boolean isEmpty
    ) {
        orderTableService.changeIsEmpty(orderTableId, isEmpty);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<Void> updateNumberOfGuest(
            @PathVariable final Long orderTableId,
            @RequestParam final int numberOfGuests
    ) {
        orderTableService.changeNumberOfGuests(orderTableId, numberOfGuests);
        return ResponseEntity.ok().build();
    }

}
