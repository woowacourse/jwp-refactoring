package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.Table;
import kitchenpos.dto.ChangeEmptyRequest;
import kitchenpos.dto.ChangeNumberOfGuestsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<Table> create() {
        final Table created = tableService.create();
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<Table>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{tableId}/empty")
    public ResponseEntity<Table> changeEmpty(
            @PathVariable final Long tableId,
            @RequestBody @Valid final ChangeEmptyRequest changeEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(tableId, changeEmptyRequest.isEmpty()));
    }

    @PutMapping("/api/tables/{tableId}/number-of-guests")
    public ResponseEntity<Table> changeNumberOfGuests(
            @PathVariable final Long tableId,
            @RequestBody @Valid final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(tableId, changeNumberOfGuestsRequest.getNumberOfGuests()));
    }
}
