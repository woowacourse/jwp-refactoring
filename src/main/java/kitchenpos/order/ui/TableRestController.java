package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.order.application.TableService;
import kitchenpos.order.application.dto.TableEmptyRequest;
import kitchenpos.order.application.dto.TableGuestRequest;
import kitchenpos.order.application.dto.TableRequest;
import kitchenpos.order.application.dto.TableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tables")
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<TableResponse> create(@RequestBody final TableRequest tableRequest) {
        final TableResponse created = tableService.create(tableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());

        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TableResponse>> list() {
        List<TableResponse> orderTables = tableService.list();

        return ResponseEntity.ok().body(orderTables);
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final TableEmptyRequest tableEmptyRequest
    ) {
        TableResponse emptyTable = tableService.changeEmpty(orderTableId, tableEmptyRequest);

        return ResponseEntity.ok().body(emptyTable);
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final TableGuestRequest tableGuestRequest
    ) {
        TableResponse changeNumberOfGuests = tableService.changeNumberOfGuests(orderTableId, tableGuestRequest);

        return ResponseEntity.ok().body(changeNumberOfGuests);
    }
}
