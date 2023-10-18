package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable2;
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
    public ResponseEntity<OrderTable2> create(@RequestBody final OrderTable2 orderTable) {
        final OrderTable2 created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable2>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable2> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTable2 orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTable))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable2> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTable2 orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTable))
                ;
    }
}
