package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.order.application.TableService;
import kitchenpos.order.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.order.dto.request.ChangeOrderTableNumberOfGuestRequest;
import kitchenpos.order.dto.request.CreateOrderTableRequest;
import kitchenpos.order.dto.response.OrderTableResponse;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final CreateOrderTableRequest request) {
        final OrderTableResponse created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created)
            ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final List<OrderTableResponse> orderTables = tableService.list();

        return ResponseEntity.ok()
            .body(orderTables)
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final ChangeOrderTableEmptyRequest request
    ) {
        final OrderTableResponse changed = tableService.changeEmpty(orderTableId, request);

        return ResponseEntity.ok()
            .body(changed)
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final ChangeOrderTableNumberOfGuestRequest request
    ) {
        final OrderTableResponse changed = tableService.changeNumberOfGuests(orderTableId, request);

        return ResponseEntity.ok()
            .body(changed)
            ;
    }
}
