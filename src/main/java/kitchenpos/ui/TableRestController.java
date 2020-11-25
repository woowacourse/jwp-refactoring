package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.dto.OrderTableChangeRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(
        @RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);
        final URI uri = URI.create("/api/tables/" + orderTableResponse.getId());
        return ResponseEntity.created(uri)
            .body(orderTableResponse)
            ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
            .body(tableService.list())
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableChangeRequest orderTableChangeRequest
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeEmpty(orderTableId, orderTableChangeRequest))
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableChangeRequest orderTableChangeRequest
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeNumberOfGuests(orderTableId, orderTableChangeRequest))
            ;
    }
}
