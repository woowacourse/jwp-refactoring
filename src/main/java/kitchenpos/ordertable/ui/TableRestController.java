package kitchenpos.ordertable.ui;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.application.dto.TableChangeEmptyStatusRequest;
import kitchenpos.ordertable.application.dto.TableChangeNumberOfGuestRequest;
import kitchenpos.ordertable.application.dto.TableCreateRequest;
import kitchenpos.ordertable.application.dto.TableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<TableResponse> create(@RequestBody final TableCreateRequest request) {
        final TableResponse created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final TableChangeEmptyStatusRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final TableChangeNumberOfGuestRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, request));
    }
}
