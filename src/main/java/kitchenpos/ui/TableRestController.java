package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.TableService;
import kitchenpos.application.dtos.GuestNumberRequest;
import kitchenpos.application.dtos.OrderTableRequest;
import kitchenpos.application.dtos.OrderTableResponse;
import kitchenpos.application.dtos.TableEmptyRequest;
import kitchenpos.domain.OrderTable;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest request) {
        final OrderTable created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(new OrderTableResponse(created));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final List<OrderTable> orderTables = tableService.list();
        final List<OrderTableResponse> response = orderTables.stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final TableEmptyRequest request
    ) {
        final OrderTable orderTable = tableService.changeEmpty(orderTableId, request);
        return ResponseEntity.ok()
                .body(new OrderTableResponse(orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final GuestNumberRequest request
    ) {
        final OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.ok()
                .body(new OrderTableResponse(orderTable));
    }
}
