package kitchenpos.order.ui;

import kitchenpos.order.application.TableService;
import kitchenpos.order.ui.dto.TableRequest;
import kitchenpos.order.ui.dto.TableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<TableResponse> create(@RequestBody @Valid final TableRequest tableRequest) {
        final TableResponse tableResponse = tableService.create(tableRequest);
        final URI uri = URI.create("/api/tables/" + tableResponse.getId());
        return ResponseEntity.created(uri)
                .body(tableResponse)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final TableRequest tableRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, tableRequest))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final TableRequest tableRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, tableRequest))
                ;
    }
}
