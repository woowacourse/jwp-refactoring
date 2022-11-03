package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.TableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;
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
        final var created = tableService.create(request);
        final var response = OrderTableResponse.from(created);
        final URI uri = URI.create("/api/tables/" + created.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final var orderTables = tableService.list();
        final var response = mapToOrderTableResponses(orderTables);

        return ResponseEntity.ok().body(response);
    }

    private List<OrderTableResponse> mapToOrderTableResponses(final List<OrderTable> tables) {
        return tables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                  @RequestBody final OrderTableRequest request) {
        final var updated = tableService.changeEmpty(orderTableId, request.isEmpty());
        final var response = OrderTableResponse.from(updated);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                           @RequestBody final OrderTableRequest request) {
        final var updated = tableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests());
        final var response = OrderTableResponse.from(updated);

        return ResponseEntity.ok().body(response);
    }
}
