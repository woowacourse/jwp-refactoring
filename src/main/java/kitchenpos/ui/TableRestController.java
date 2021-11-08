package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.TableRequest;
import kitchenpos.ui.dto.TableResponse;
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
    public ResponseEntity<TableResponse> create(@RequestBody final TableRequest tableRequest) {
        final TableResponse newTable = tableService.create(tableRequest);
        final URI uri = URI.create("/api/tables/" + newTable.getId());
        return ResponseEntity.created(uri)
                .body(newTable);
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> findAll() {
        List<TableResponse> tables = tableService.findAll();
        return ResponseEntity.ok()
                .body(tables);
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                  @RequestBody final TableRequest tableRequest) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, tableRequest));
                .body(tableService.changeEmpty(orderTableId, orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                           @RequestBody final TableRequest tableRequest) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, tableRequest));
                .body(tableService.changeNumberOfGuests(orderTableId, orderTable));
    }
}
