package kitchenpos.ordertable;

import kitchenpos.ordertable.TableService;
import kitchenpos.ordertable.OrderTableDto;
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
    public ResponseEntity<OrderTableDto> create(@RequestBody final OrderTableDto orderTable) {
        final OrderTableDto created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableDto>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableDto> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableDto orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTable))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableDto> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableDto orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTable));
    }
}
