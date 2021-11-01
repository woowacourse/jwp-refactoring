package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.OrderTableCreateRequest;
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
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTableCreateRequest request) {
        final OrderTable created = tableService.create(request.toEntity());
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
            @RequestBody final OrderTableChangeEmptyRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, request.toEntity()))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeNumberOfGuestsRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, request.toEntity()))
                ;
    }
}
