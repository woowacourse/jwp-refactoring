package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.application.dto.TableDto;
import kitchenpos.ui.dto.ChangeTableEmptyRequest;
import kitchenpos.ui.dto.TableGuestNumberRequest;
import kitchenpos.ui.dto.CreateTableRequest;
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
    public ResponseEntity<TableDto> create(@RequestBody final CreateTableRequest createTableRequest) {
        final TableDto created = tableService.create(createTableRequest.getNumberOfGuests(), createTableRequest.getEmpty());
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableDto>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableDto> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeTableEmptyRequest changeTableEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, changeTableEmptyRequest.getEmpty()))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableDto> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final TableGuestNumberRequest tableGuestNumberRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, tableGuestNumberRequest.getNumberOfGuests()))
                ;
    }
}
