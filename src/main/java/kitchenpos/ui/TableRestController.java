package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableResponse;
import kitchenpos.ui.dto.TableUpdateEmptyRequest;
import kitchenpos.ui.dto.TableUpdateGuestNumberRequest;
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
    public ResponseEntity<TableResponse> create(@RequestBody final TableCreateRequest request) {
        final TableResponse tableResponse = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + tableResponse.getId());

        return ResponseEntity.created(uri).body(tableResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                     @RequestBody final TableUpdateEmptyRequest request) {
        final TableResponse response = tableService.changeEmpty(orderTableId, request);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                              @RequestBody final TableUpdateGuestNumberRequest request) {
        final TableResponse tableResponse = tableService.changeNumberOfGuests(orderTableId, request);

        return ResponseEntity.ok().body(tableResponse);
    }
}
