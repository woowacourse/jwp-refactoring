package kitchenpos.legacy.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.legacy.application.LegacyTableService;
import kitchenpos.legacy.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final LegacyTableService tableService;

    public TableController(LegacyTableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<OrderTable> create(@RequestBody OrderTable orderTable) {
        OrderTable created = tableService.create(orderTable);
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderTable>> findAll() {
        return ResponseEntity.ok()
            .body(tableService.findAll());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
        @PathVariable Long orderTableId,
        @RequestBody OrderTable orderTable
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeEmpty(orderTableId, orderTable));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
        @PathVariable Long orderTableId,
        @RequestBody OrderTable orderTable
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeNumberOfGuests(orderTableId, orderTable));
    }
}
