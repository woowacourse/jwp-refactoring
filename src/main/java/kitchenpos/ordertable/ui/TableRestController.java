package kitchenpos.ordertable.ui;

import kitchenpos.order.application.TableService;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.application.request.ChangeEmptyRequest;
import kitchenpos.ordertable.application.request.NumberOfGuestsRequest;
import kitchenpos.ordertable.application.request.OrderTableRequest;
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
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTableRequest request) {
        final OrderTable created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeEmptyRequest request) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final NumberOfGuestsRequest request) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, request));
    }
}
