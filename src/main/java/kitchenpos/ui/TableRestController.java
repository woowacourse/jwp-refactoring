package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest orderTableRequest) {

        final OrderTable created = tableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());

        final OrderTableResponse orderTableResponse = OrderTableResponse.from(created);

        return ResponseEntity.created(uri)
                .body(orderTableResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {

        final List<OrderTable> orderTables = tableService.list();

        final List<OrderTableResponse> orderTableResponses = orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(orderTableResponses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody OrderTableRequest orderTableRequest) {

        final OrderTable orderTable = tableService.changeEmpty(orderTableId, orderTableRequest);

        final OrderTableResponse orderTableResponse = OrderTableResponse.from(orderTable);

        return ResponseEntity.ok()
                .body(orderTableResponse);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody OrderTableRequest orderTableRequest) {

        final OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

        final OrderTableResponse orderTableResponse = OrderTableResponse.from(orderTable);

        return ResponseEntity.ok()
                .body(orderTableResponse);
    }
}
