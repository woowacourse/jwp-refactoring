package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
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
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTable orderTable) {
        final OrderTable createdOrderTable = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + createdOrderTable.getId());

        return ResponseEntity.created(uri)
            .body(createdOrderTable);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        List<OrderTable> orderTables = tableService.list();

        return ResponseEntity.ok()
            .body(orderTables);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(@PathVariable final Long orderTableId,
        @RequestBody final OrderTable orderTable) {
        OrderTable changedOrderTable = tableService.changeEmpty(orderTableId, orderTable);

        return ResponseEntity.ok()
            .body(changedOrderTable);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(@PathVariable final Long orderTableId,
        @RequestBody final OrderTable orderTable) {
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTableId, orderTable);

        return ResponseEntity.ok()
            .body(changedOrderTable);
    }
}
