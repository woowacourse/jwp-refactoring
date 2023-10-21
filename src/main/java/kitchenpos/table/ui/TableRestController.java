package kitchenpos.table.ui;

import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.OrderTableCreateRequest;
import kitchenpos.table.application.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<Void> create(@RequestBody final OrderTableCreateRequest request) {
        final Long id = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + id);
        return ResponseEntity.created(uri)
                .build()
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<Long> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final boolean empty
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, empty))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<Long> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final int numberOfGuests
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, numberOfGuests))
                ;
    }
}
