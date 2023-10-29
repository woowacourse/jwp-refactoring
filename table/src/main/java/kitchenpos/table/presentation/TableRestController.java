package kitchenpos.table.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableIsEmptyUpdateRequest;
import kitchenpos.table.dto.OrderTableNumberOfGuestsUpdateRequest;
import kitchenpos.table.dto.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateRequest request) {
        OrderTableResponse response = tableService.create(request);
        return ResponseEntity.created(URI.create("/api/tables/" + response.getId())).body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> orderTables = tableService.list();
        return ResponseEntity.ok().body(orderTables);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableIsEmptyUpdateRequest request
    ) {
        OrderTableResponse response = tableService.changeIsEmpty(orderTableId, request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableNumberOfGuestsUpdateRequest request
    ) {
        OrderTableResponse response = tableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.ok().body(response);
    }
}
