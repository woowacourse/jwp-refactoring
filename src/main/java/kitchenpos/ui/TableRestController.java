package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.dto.ordertable.OrderTableChangeEmptyRequest;
import kitchenpos.dto.ordertable.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> respondCreatedOrderTableResponse(
            @RequestBody final OrderTableCreateRequest orderTableCreateRequest
    ) {
        final OrderTableResponse created = tableService.createOrderTable(orderTableCreateRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> respondAllOrderTableResponses() {
        return ResponseEntity.ok()
                .body(tableService.listAllOrderTables());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> respondEmptyChangedOrderTableResponse(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTableChangeEmptyRequest));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> respondNumberOfGuestsChangedOrderTableResponse(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest
            ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTableChangeNumberOfGuestsRequest));
    }
}
