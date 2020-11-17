package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.dto.order.OrderTableChangeEmptyRequest;
import kitchenpos.dto.order.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.order.OrderTableCreateRequest;
import kitchenpos.dto.order.OrderTableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TableRestController {
    private final TableService tableService;

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody @Valid final OrderTableCreateRequest request) {
        final OrderTableResponse created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());

        return ResponseEntity.created(uri).
                body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final OrderTableChangeEmptyRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final OrderTableChangeNumberOfGuestsRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, request));
    }
}
