package kitchenpos.ordertable.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.dto.request.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.ordertable.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.response.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(
        @RequestBody final OrderTableCreateRequest request) {
        final OrderTableResponse response = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + response.getId());

        return ResponseEntity.created(uri)
            .body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final List<OrderTableResponse> responses = tableService.list();

        return ResponseEntity.ok()
            .body(responses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableUpdateEmptyRequest request
    ) {
        final OrderTableResponse response = tableService.changeEmpty(orderTableId, request);

        return ResponseEntity.ok()
            .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableUpdateNumberOfGuestsRequest request
    ) {
        final OrderTableResponse response = tableService.changeNumberOfGuests(orderTableId,
            request);

        return ResponseEntity.ok()
            .body(response);
    }
}
