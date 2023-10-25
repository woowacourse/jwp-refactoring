package kitchenpos.ui.order;

import kitchenpos.application.order.TableService;
import kitchenpos.application.order.request.TableCreateRequest;
import kitchenpos.application.order.request.TableUpdateRequest;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.ui.order.response.TableResponse;
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
    public ResponseEntity<TableResponse> create(@RequestBody TableCreateRequest request) {
        final OrderTable orderTable = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + orderTable.getId());
        return ResponseEntity.created(uri).body(TableResponse.of(orderTable));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody TableUpdateRequest updateRequest) {
        OrderTable orderTable = tableService.changeEmpty(orderTableId, updateRequest);
        return ResponseEntity.ok().body(TableResponse.of(orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody TableUpdateRequest updateRequest) {
        OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, updateRequest);
        return ResponseEntity.ok().body(TableResponse.of(orderTable));
    }
}
