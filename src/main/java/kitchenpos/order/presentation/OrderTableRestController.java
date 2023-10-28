package kitchenpos.order.presentation;

import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.application.dto.OrderTableCreateRequest;
import kitchenpos.order.application.dto.OrderTableResponse;
import kitchenpos.order.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class OrderTableRestController {
    private final OrderTableService orderTableService;

    public OrderTableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTableCreateRequest request) {
        final Long id = orderTableService.create(request);
        return ResponseEntity.created(URI.create("/api/tables/" + id)).build();
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> findAll() {
        final List<OrderTableResponse> orderTableResponses = orderTableService.findAll();
        return ResponseEntity.ok().body(orderTableResponses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<Void> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final boolean isEmpty
    ) {
        orderTableService.changeEmpty(orderTableId, isEmpty);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final Integer numberOfGuests
    ) {
        orderTableService.changeNumberOfGuests(orderTableId, numberOfGuests);
        return ResponseEntity.ok().build();
    }
}
