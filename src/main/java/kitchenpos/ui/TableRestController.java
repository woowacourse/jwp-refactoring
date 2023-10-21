package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.application.TableService;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.ui.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.request.OrderTableCreateRequest;
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
    public ResponseEntity<OrderTable> create(
            @Valid @RequestBody final OrderTableCreateRequest request
    ) {
        final var response = tableService.create(request);
        final var uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        final var response = tableService.list();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @Valid @RequestBody final OrderTableChangeEmptyRequest request
    ) {
        final var response = tableService.changeEmpty(orderTableId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @Valid @RequestBody final OrderTableChangeNumberOfGuestsRequest request
    ) {
        final var response = tableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.ok(response);
    }
}
