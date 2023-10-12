package kitchenpos.ui.table;

import kitchenpos.application.table.TableService;
import kitchenpos.application.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.table.dto.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.application.table.dto.OrderTableCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.table.dto.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = tableService.create(orderTableCreateRequest);
        URI uri = URI.create("/api/tables/" + orderTable.getId());

        return ResponseEntity.created(uri)
                .body(OrderTableResponse.from(orderTable));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> response = tableService.list()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest
    ) {
        OrderTable orderTable = tableService.changeEmpty(orderTableId, orderTableChangeEmptyRequest);

        return ResponseEntity.ok()
                .body(OrderTableResponse.from(orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeNumberOfGuestRequest req
    ) {
        OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, req);

        return ResponseEntity.ok()
                .body(OrderTableResponse.from(orderTable));
    }
}
