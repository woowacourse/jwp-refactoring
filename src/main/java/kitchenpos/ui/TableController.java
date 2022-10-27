package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableController {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTable> create(@RequestBody OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable created = tableService.create(orderTableCreateRequest);
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTableChangeEmptyRequest));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTableChangeNumberOfGuestsRequest));
    }
}
