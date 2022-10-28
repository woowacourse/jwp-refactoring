package kitchenpos.table.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.presentation.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.presentation.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.presentation.dto.OrderTableCreateRequest;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableCreateRequest request) {
        OrderTableResponse response = tableService.create(request.toRequest());
        final URI uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable Long orderTableId,
                                                          @RequestBody OrderTableChangeEmptyRequest request) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, request.isEmpty()));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable Long orderTableId,
                                                                   @RequestBody OrderTableChangeNumberOfGuestsRequest request) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests()));
    }
}
