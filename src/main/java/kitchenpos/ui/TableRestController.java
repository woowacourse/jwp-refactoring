package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.OrderTableCreateRequest;
import kitchenpos.ui.request.OrderTableUpdateEmptyRequest;
import kitchenpos.ui.request.OrderTableUpdateNumberOfGuestsRequest;
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
    public ResponseEntity<OrderTable> create(@RequestBody OrderTableCreateRequest request) {
        OrderTable response = tableService.create(request);
        URI uri = URI.create("/api/tables/" + response.getId());

        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        List<OrderTable> responses = tableService.list();

        return ResponseEntity.ok()
                .body(responses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableUpdateEmptyRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableUpdateNumberOfGuestsRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, request));
    }
}
