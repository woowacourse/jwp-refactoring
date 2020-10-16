package kitchenpos.ui;

import static kitchenpos.ui.TableRestController.*;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping(API_TABLES)
@RestController
public class TableRestController {
    public static final String API_TABLES = "/api/tables";

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTable orderTable) {
        final OrderTable created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTable orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTable))
                ;
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTable orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTable))
                ;
    }
}
