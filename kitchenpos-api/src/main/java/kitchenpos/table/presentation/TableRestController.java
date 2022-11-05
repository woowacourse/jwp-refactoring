package kitchenpos.table.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.table.presentation.dto.OrderTableCreateRequest;
import kitchenpos.table.presentation.dto.OrderTableEmptyRequest;
import kitchenpos.table.presentation.dto.OrderTableGuestRequest;
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
            @RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest.toCommand());
        final URI uri = URI.create("/api/tables/" + orderTableResponse.id());
        return ResponseEntity.created(uri)
                .body(orderTableResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableEmptyRequest orderTableEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTableEmptyRequest.toCommand()));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableGuestRequest orderTableGuestRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTableGuestRequest.toCommand()));
    }
}
