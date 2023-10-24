package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.ui.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.ui.dto.request.ChangeOrderTableGuestRequest;
import kitchenpos.ui.dto.request.CreateOrderTableRequest;
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
    public ResponseEntity<OrderTable> create(@RequestBody final CreateOrderTableRequest createOrderTableRequest) {
        final OrderTable created = tableService.create(createOrderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeOrderTableEmptyRequest changeOrderTableEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, changeOrderTableEmptyRequest))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeOrderTableGuestRequest changeOrderTableGuestRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, changeOrderTableGuestRequest))
                ;
    }
}
