package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kitchenpos.table.application.TableService;
import kitchenpos.table.ui.request.ChangeTableEmptyRequest;
import kitchenpos.table.ui.request.ChangeTableGuestRequest;
import kitchenpos.table.ui.request.CreateTableRequest;
import kitchenpos.table.ui.response.TableResponse;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<TableResponse> create(@RequestBody final CreateTableRequest request) {
        final TableResponse created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok()
                             .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeTableEmptyRequest request
    ) {
        return ResponseEntity.ok()
                             .body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeTableGuestRequest request
    ) {
        return ResponseEntity.ok()
                             .body(tableService.changeNumberOfGuests(orderTableId, request));
    }
}
