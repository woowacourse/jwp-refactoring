package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.table.dto.request.ChangeOrderTableNumberOfGuestRequest;
import kitchenpos.table.dto.request.CreateOrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final CreateOrderTableRequest request) {
        final OrderTable created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(new OrderTableResponse(created))
            ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> orderTables = tableService.list().stream()
            .map(it -> new OrderTableResponse(it))
            .collect(Collectors.toList());

        return ResponseEntity.ok()
            .body(orderTables)
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final ChangeOrderTableEmptyRequest request
    ) {
        OrderTable changed = tableService.changeEmpty(orderTableId, request);
        return ResponseEntity.ok()
            .body(new OrderTableResponse(changed))
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final ChangeOrderTableNumberOfGuestRequest request
    ) {
        OrderTable changed = tableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.ok()
            .body(new OrderTableResponse(changed))
            ;
    }
}
