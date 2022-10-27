package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.OrderTableEmptyRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableResponse;
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

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest orderTableRequest) {
        OrderTableResponse created = tableService.create(orderTableRequest);
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(@PathVariable Long orderTableId,
                                                  @RequestBody OrderTableEmptyRequest orderTableEmptyRequest) {
        tableService.changeEmpty(orderTableId, orderTableEmptyRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(@PathVariable Long orderTableId,
                                                           @RequestBody OrderTable orderTable) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTable));
    }
}
