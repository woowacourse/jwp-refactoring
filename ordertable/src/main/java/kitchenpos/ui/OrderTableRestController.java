package kitchenpos.ui;

import kitchenpos.application.OrderTableService;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableUpdateRequest;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateRequest request) {
        final OrderTableResponse orderTable = orderTableService.create(request);
        final URI uri = URI.create("/api/tables/" + orderTable.getId());

        return ResponseEntity.created(uri).body(orderTable);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final List<OrderTableResponse> orderTables = orderTableService.list();
        return ResponseEntity.ok().body(orderTables);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                          @RequestBody final OrderTableUpdateRequest request) {
        final OrderTableResponse orderTable = orderTableService.changeEmpty(orderTableId, request);
        return ResponseEntity.ok().body(orderTable);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                   @RequestBody final OrderTableUpdateRequest request) {
        final OrderTableResponse orderTable = orderTableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.ok().body(orderTable);
    }
}
