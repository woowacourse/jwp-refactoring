package kitchenpos.table.ui;

import kitchenpos.table.application.TableService;
import kitchenpos.table.application.request.TableCreateRequest;
import kitchenpos.table.application.request.TableEmptyUpdateRequest;
import kitchenpos.table.application.request.TableGuestUpdateRequest;
import kitchenpos.table.application.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody TableCreateRequest orderTable) {
        OrderTableResponse created = tableService.create(orderTable);
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody TableEmptyUpdateRequest tableEmptyUpdateRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, tableEmptyUpdateRequest))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody TableGuestUpdateRequest tableGuestUpdateRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, tableGuestUpdateRequest));
    }
}
