package kitchenpos.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.TableService;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableUpdateRequest;

@RestController
public class TableRestController {

    private final TableService tableService;

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest orderTableRequest) {
        OrderTableResponse created = tableService.create(orderTableRequest);
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
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
        return ResponseEntity.ok()
            .body(tableService.changeEmpty(orderTableId, orderTableUpdateRequest.getEmpty()));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable Long orderTableId,
        @RequestBody OrderTableUpdateRequest orderTableUpdateRequest
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeNumberOfGuests(
                orderTableId,
                orderTableUpdateRequest.getNumberOfGuests()
            ));
    }
}
