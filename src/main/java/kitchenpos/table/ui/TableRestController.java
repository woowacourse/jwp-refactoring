package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;

import kitchenpos.table.application.TableService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.dto.table.CreateOrderTableRequest;
import kitchenpos.table.dto.table.UpdateTableGuestRequest;
import kitchenpos.table.dto.table.UpdateTableStatusRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestController {

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTable> create(@RequestBody final CreateOrderTableRequest createOrderTableRequest) {
        final OrderTable created = tableService.create(createOrderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        final List<OrderTable> orderTables = tableService.list();
        return ResponseEntity.ok(orderTables);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final UpdateTableStatusRequest updateTableStatusRequest
    ) {
        final OrderTable orderTable = tableService.changeEmpty(orderTableId, updateTableStatusRequest);
        return ResponseEntity.ok(orderTable);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final UpdateTableGuestRequest updateTableGuestRequest
    ) {
        final OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, updateTableGuestRequest);
        return ResponseEntity.ok(orderTable);
    }
}
