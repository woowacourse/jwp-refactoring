package kitchenpos.ui.table;

import java.net.URI;
import java.util.List;
import kitchenpos.application.table.TableService;
import kitchenpos.application.table.dto.ChangeOrderTableEmptyResponse;
import kitchenpos.application.table.dto.ChangeOrderTableNumberOfGuestsResponse;
import kitchenpos.application.table.dto.CreateOrderTableResponse;
import kitchenpos.application.table.dto.SearchOrderTableResponse;
import kitchenpos.ui.table.dto.ChangeOrderTableEmptyRequest;
import kitchenpos.ui.table.dto.ChangeOrderTableNumberOfGuestsRequest;
import kitchenpos.ui.table.dto.CreateOrderTableRequest;
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
    public ResponseEntity<CreateOrderTableResponse> create(@RequestBody CreateOrderTableRequest request) {
        CreateOrderTableResponse response = tableService.create(request.toCommand());
        URI uri = URI.create("/api/tables/" + response.id());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<SearchOrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<ChangeOrderTableEmptyResponse> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody ChangeOrderTableEmptyRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(request.toCommand(orderTableId)));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<ChangeOrderTableNumberOfGuestsResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody ChangeOrderTableNumberOfGuestsRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(request.toCommand(orderTableId)));
    }
}
