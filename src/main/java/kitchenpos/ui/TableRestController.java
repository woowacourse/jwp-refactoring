package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.response.OrderTableResponse;
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
        final OrderTable created = tableService.create(request.getNumberOfGuests(), request.isEmpty());

        final OrderTableResponse body = new OrderTableResponse(
                created.getId(), created.getTableGroupId(),
                created.getNumberOfGuests(), created.isEmpty()
        );
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(body);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> body = tableService.list().stream()
                .map(t -> new OrderTableResponse(t.getId(), t.getTableGroupId(), t.getNumberOfGuests(), t.isEmpty()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(body);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest request
    ) {
        OrderTable orderTable = tableService.changeEmpty(orderTableId, request.isEmpty());
        OrderTableResponse body = new OrderTableResponse(
                orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty()
        );
        return ResponseEntity.ok().body(body);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest request
    ) {
        OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests());
        OrderTableResponse body = new OrderTableResponse(
                orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty()
        );
        return ResponseEntity.ok().body(body);
    }
}
