package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableEmptyChangeRequest;
import kitchenpos.table.dto.OrderTableNumberOfGuestChangeRequest;
import kitchenpos.table.dto.OrderTableResponse;
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
        final OrderTableResponse created = tableService.create(orderTableCreateRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final List<OrderTableResponse> orderTableResponses = tableService.list();
        return ResponseEntity.ok().body(orderTableResponses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableEmptyChangeRequest orderTableEmptyChangeRequest
    ) {
        final OrderTableResponse orderTable = tableService.changeEmpty(orderTableId,
                orderTableEmptyChangeRequest.isEmpty());
        return ResponseEntity.ok().body(orderTable);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableNumberOfGuestChangeRequest orderTableNumberOfGuestChangeRequest
    ) {
        final OrderTableResponse orderTable = tableService.changeNumberOfGuests(orderTableId,
                orderTableNumberOfGuestChangeRequest.getNumberOfGuests());
        return ResponseEntity.ok().body(orderTable);
    }
}
