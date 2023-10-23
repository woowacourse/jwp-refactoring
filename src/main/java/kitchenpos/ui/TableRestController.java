package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.request.OrderTableUpdateNumberOfGuestRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/tables")
@RestController
public class TableRestController {

    private final TableService tableService;

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create() {
        OrderTableResponse created = tableService.create();
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableUpdateEmptyRequest orderTableRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTableRequest));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableUpdateNumberOfGuestRequest orderTableRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTableRequest));
    }
}
