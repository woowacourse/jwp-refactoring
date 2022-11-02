package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.request.OrderTableRequest;
import kitchenpos.table.application.dto.request.TableEmptyRequest;
import kitchenpos.table.application.dto.request.TableNumberOfGuestsRequest;
import kitchenpos.table.domain.OrderTable;
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
    public ResponseEntity<Void> create(@RequestBody OrderTableRequest orderTableRequest) {
        Long tableId = tableService.create(orderTableRequest);
        URI uri = URI.create("/api/tables/" + tableId);
        return ResponseEntity.created(uri)
                .build();
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(@PathVariable Long orderTableId,
                                                  @RequestBody TableEmptyRequest tableEmptyRequest) {
        tableService.changeEmpty(orderTableId, tableEmptyRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(@PathVariable Long orderTableId,
                                                           @RequestBody TableNumberOfGuestsRequest tableNumberOfGuestsRequest) {
        tableService.changeNumberOfGuests(orderTableId, tableNumberOfGuestsRequest);
        return ResponseEntity.ok().build();
    }
}
