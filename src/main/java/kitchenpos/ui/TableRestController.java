package kitchenpos.ui;

import java.net.URI;
import java.util.List;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest request) {
        final OrderTable created = tableService.create(request);

        final URI uri = URI.create("/api/tables/" + created.getId());
        final OrderTableResponse body = OrderTableResponse.from(created);
        return ResponseEntity.created(uri).body(body);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final List<OrderTable> foundTables = tableService.list();

        final List<OrderTableResponse> body = OrderTableResponse.from(foundTables);
        return ResponseEntity.ok().body(body);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                          @RequestBody final OrderTableRequest orderTable) {
        final OrderTable changedTable = tableService.changeEmpty(orderTableId, orderTable);

        final OrderTableResponse body = OrderTableResponse.from(changedTable);
        return ResponseEntity.ok().body(body);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                   @RequestBody final OrderTableRequest orderTable) {
        final OrderTable changedTable = tableService.changeNumberOfGuests(orderTableId, orderTable);

        final OrderTableResponse body = OrderTableResponse.from(changedTable);
        return ResponseEntity.ok().body(body);
    }
}
