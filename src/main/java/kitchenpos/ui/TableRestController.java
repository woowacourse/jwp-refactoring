package kitchenpos.ui;

import java.util.stream.Collectors;
import kitchenpos.table.application.TableService;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableGuestNumberRequest;
import kitchenpos.dto.request.OrderTableSetEmptyRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTable created = tableService.create(orderTableCreateRequest);
        final OrderTableResponse response = OrderTableResponse.from(created);
        final URI uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final List<OrderTableResponse> response = tableService.list().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableSetEmptyRequest orderTableSetEmptyRequest
    ) {
        final OrderTable orderTable = tableService.changeEmpty(orderTableId, orderTableSetEmptyRequest);
        final OrderTableResponse response = OrderTableResponse.from(orderTable);
        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableGuestNumberRequest orderTableGuestNumberRequest
    ) {
        final OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, orderTableGuestNumberRequest);
        final OrderTableResponse response = OrderTableResponse.from(orderTable);
        return ResponseEntity.ok()
                .body(response);
    }
}
