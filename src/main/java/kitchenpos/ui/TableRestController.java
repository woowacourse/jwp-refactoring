package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.application.TableService;
import kitchenpos.ui.dto.TableChangeEmptyRequest;
import kitchenpos.ui.dto.TableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableResponse;
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
    public ResponseEntity<TableResponse> create(
        @RequestBody @Valid final TableCreateRequest tableCreateRequest) {
        final TableResponse tableResponse = tableService.create(tableCreateRequest);
        final URI uri = URI.create("/api/tables/" + tableResponse.getId());

        return ResponseEntity.created(uri)
            .body(tableResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> list() {
        List<TableResponse> tables = tableService.list();

        return ResponseEntity.ok()
            .body(tables);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(@PathVariable final Long orderTableId,
        @RequestBody @Valid final TableChangeEmptyRequest tableChangeEmptyRequest) {
        TableResponse changedTable = tableService
            .changeEmpty(orderTableId, tableChangeEmptyRequest);

        return ResponseEntity.ok()
            .body(changedTable);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
        @RequestBody @Valid final TableChangeNumberOfGuestsRequest tableChangeNumberOfGuestsRequest) {
        TableResponse changedTable = tableService
            .changeNumberOfGuests(orderTableId, tableChangeNumberOfGuestsRequest);

        return ResponseEntity.ok()
            .body(changedTable);
    }
}
