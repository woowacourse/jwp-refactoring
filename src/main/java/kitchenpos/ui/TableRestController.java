package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.domain.OrderTable;
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
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTableCreateRequest request) {
        final OrderTable created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PatchMapping(value = "/api/tables/{orderTableId}", params = "empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestParam final boolean empty
    ) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, empty));
    }

    @PatchMapping(value = "/api/tables/{orderTableId}", params = "numberOfGuests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestParam final int numberOfGuests
    ) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, numberOfGuests));
    }
}
