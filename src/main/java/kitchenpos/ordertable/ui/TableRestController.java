package kitchenpos.ordertable.ui;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.request.OrderTableCreateRequest;
import kitchenpos.request.TableChangeEmptyRequest;
import kitchenpos.request.TableChangeNumberOfGuestsRequest;
import kitchenpos.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableCreateRequest request) {
        OrderTable created = tableService.create(request);
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(OrderTableResponse.from(created));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> response = tableService.list().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody TableChangeEmptyRequest request
    ) {
        OrderTableResponse response = OrderTableResponse.from(tableService.changeEmpty(orderTableId, request));
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody TableChangeNumberOfGuestsRequest request
    ) {
        OrderTableResponse response = OrderTableResponse.from(tableService.changeNumberOfGuests(orderTableId, request));
        return ResponseEntity.ok().body(response);
    }
}
