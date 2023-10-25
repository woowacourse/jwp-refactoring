package kitchenpos.ordertable.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.request.OrderTableCreateRequest;
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
    public ResponseEntity<Long> create(@RequestBody final OrderTableCreateRequest request) {
        final Long id = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + id);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<Void> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeEmptyRequest request
    ) {
        tableService.changeEmpty(orderTableId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<Void> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeNumberOfGuestsRequest request
    ) {
        tableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.ok().build();
    }
}
