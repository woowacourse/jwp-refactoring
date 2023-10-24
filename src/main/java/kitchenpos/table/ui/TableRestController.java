package kitchenpos.table.ui;

import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.request.OrderTableEmptyModifyRequest;
import kitchenpos.table.application.dto.request.OrderTableNumberOfGuestModifyRequest;
import kitchenpos.table.application.dto.response.OrderTableQueryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableQueryResponse> create(
            @RequestBody final OrderTableCreateRequest request) {
        final OrderTableQueryResponse created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableQueryResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PatchMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableQueryResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableEmptyModifyRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, request));
    }

    @PatchMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableQueryResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableNumberOfGuestModifyRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, request));
    }
}
