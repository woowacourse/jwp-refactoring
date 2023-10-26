package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.CreateOrderTableRequest;
import kitchenpos.ui.dto.PutOrderTableEmptyRequest;
import kitchenpos.ui.dto.PutOrderTableGuestsNumberRequest;
import kitchenpos.ui.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final CreateOrderTableRequest orderTableRequest) {
        final OrderTable orderTable = tableService.create(orderTableRequest);
        final OrderTableResponse response = toResponse(orderTable);
        final URI uri = URI.create("/api/tables/" + orderTable.getId());
        return ResponseEntity.created(uri)
                             .body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final List<OrderTableResponse> responses = tableService.list().stream()
                                                               .map(this::toResponse)
                                                               .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok()
                             .body(responses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final PutOrderTableEmptyRequest orderTableRequest
    ) {
        final OrderTable orderTable = tableService.changeEmpty(orderTableId, orderTableRequest);
        return ResponseEntity.ok()
                             .body(toResponse(orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final PutOrderTableGuestsNumberRequest orderTableGuestsNumberRequest
    ) {
        final OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, orderTableGuestsNumberRequest);
        return ResponseEntity.ok()
                             .body(toResponse(orderTable));
    }

    private OrderTableResponse toResponse(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroup(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }
}
