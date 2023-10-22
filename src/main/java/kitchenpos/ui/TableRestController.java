package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.application.request.OrderTableCreateRequest;
import kitchenpos.application.request.OrderTableUpdateRequest;
import kitchenpos.application.response.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(
            @RequestBody OrderTableCreateRequest orderTableCreateRequest
    ) {
        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);
        URI uri = URI.create("/api/tables/" + orderTableResponse.getId());
        return ResponseEntity.created(uri)
                .body(orderTableResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableUpdateRequest orderTableUpdateRequest
    ) {
        OrderTableResponse orderTableResponse = tableService.changeEmpty(orderTableId, orderTableUpdateRequest);
        return ResponseEntity.ok()
                .body(orderTableResponse);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableUpdateRequest orderTableUpdateRequest
    ) {
        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(orderTableId,
                orderTableUpdateRequest);
        return ResponseEntity.ok()
                .body(orderTableResponse);
    }
}
