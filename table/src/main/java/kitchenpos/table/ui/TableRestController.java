package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.table.dto.request.TableCreationRequest;
import kitchenpos.table.dto.request.TableEmptyUpdateRequest;
import kitchenpos.table.dto.request.TableNumberOfGuestsUpdateRequest;
import kitchenpos.table.dto.response.TableResponse;
import kitchenpos.table.application.TableService;
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

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<TableResponse> create(@Valid @RequestBody TableCreationRequest request) {
        TableResponse response = tableService.create(request);
        URI uri = URI.create("/api/tables/" + response.getId());

        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable Long orderTableId,
            @Valid @RequestBody TableEmptyUpdateRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @Valid @RequestBody TableNumberOfGuestsUpdateRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, request));
    }

}
