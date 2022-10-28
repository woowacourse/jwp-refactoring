package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.order.application.TableService;
import kitchenpos.order.ui.dto.TableChangeEmptyRequest;
import kitchenpos.order.ui.dto.TableChangeGuestNumberRequest;
import kitchenpos.order.ui.dto.TableCreateRequest;
import kitchenpos.order.ui.dto.TableResponse;
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
    public ResponseEntity<TableResponse> create(@Valid @RequestBody final TableCreateRequest request) {
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

    @PutMapping("/api/tables/{tableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(@PathVariable final Long tableId,
                                                     @Valid @RequestBody final TableChangeEmptyRequest request) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(tableId, request));
    }

    @PutMapping("/api/tables/{tableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(@PathVariable final Long tableId,
                                                              @Valid @RequestBody final TableChangeGuestNumberRequest request) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(tableId, request));
    }
}
