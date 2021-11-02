package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.request.OrderTableChangeGuestsRequest;
import kitchenpos.ui.request.OrderTableCreateRequest;
import kitchenpos.ui.request.OrderTableRequest;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateRequest orderTable) {
        final OrderTableResponse created = tableService.create(orderTable.toEntity());
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeEmptyRequest orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTable.isEmpty()));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableChangeGuestsRequest orderTable
        ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTable.getNumberOfGuests()));
    }
}
