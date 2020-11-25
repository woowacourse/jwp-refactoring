package kitchenpos.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.TableService;
import kitchenpos.domain.Table;
import kitchenpos.dto.ChangeEmptyRequest;
import kitchenpos.dto.ChangeNumberOfGuestsRequest;

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
            @RequestBody final ChangeEmptyRequest changeEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(tableId, changeEmptyRequest.isEmpty()));
    }

    @PutMapping("/api/tables/{tableId}/number-of-guests")
    public ResponseEntity<Table> changeNumberOfGuests(
            @PathVariable final Long tableId,
            @RequestBody final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(tableId, changeNumberOfGuestsRequest.getNumberOfGuests()));
    }
}
