package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.application.TableService;
import kitchenpos.dto.request.TableEmptyUpdateRequest;
import kitchenpos.dto.request.TableGuestUpdateRequest;
import kitchenpos.dto.request.TableRequest;
import kitchenpos.dto.response.TableResponse;
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
    public ResponseEntity<Void> create(@RequestBody @Valid final TableRequest request) {
        final Long tableId = tableService.create(request);
        return ResponseEntity.created(URI.create("/api/tables/" + tableId)).build();
    }

    @GetMapping
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final TableEmptyUpdateRequest request
    ) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final TableGuestUpdateRequest request
    ) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, request));
    }
}
