package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateRequest request) {
        final var created = tableService.create(request);
        final var orderTableResponse = OrderTableResponse.from(created);
        final var uri = URI.create("/api/tables/" + created.getId());

        return ResponseEntity.created(uri)
                .body(orderTableResponse)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final var orderTables = tableService.list();
        final var orderTableResponses = OrderTableResponse.from(orderTables);

        return ResponseEntity.ok()
                .body(orderTableResponses)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeEmptyRequest request
    ) {
        final var changedTable = tableService.changeEmpty(orderTableId, request);
        final var orderTableResponse = OrderTableResponse.from(changedTable);

        return ResponseEntity.ok()
                .body(orderTableResponse)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeNumberOfGuestsRequest request
    ) {
        final var changedTable = tableService.changeNumberOfGuests(orderTableId, request);
        final var orderTableResponse = OrderTableResponse.from(changedTable);

        return ResponseEntity.ok()
                .body(orderTableResponse)
                ;
    }
}
