package kitchenpos.table.ui;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableEmptyStatusUpdateRequest;
import kitchenpos.table.dto.TableNumberOfGuestsUpdateRequest;
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
    public ResponseEntity<OrderTable> create(@RequestBody final TableCreateRequest tableCreateRequest) {
        final OrderTable created = tableService.create(tableCreateRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final TableEmptyStatusUpdateRequest tableEmptyStatusUpdateRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, tableEmptyStatusUpdateRequest));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final TableNumberOfGuestsUpdateRequest tableNumberOfGuestsUpdateRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, tableNumberOfGuestsUpdateRequest));
    }
}
