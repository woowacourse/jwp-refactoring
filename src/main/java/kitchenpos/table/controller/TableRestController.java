package kitchenpos.table.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.table.domain.Table;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableEmptyEditRequest;
import kitchenpos.table.dto.TableGuestEditRequest;
import kitchenpos.table.service.TableService;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<Table> create(@RequestBody @Valid TableCreateRequest request) {
        Long id = tableService.create(request);
        URI uri = URI.create("/api/tables/" + id);
        return ResponseEntity.created(uri)
            .build();
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<Table>> list() {
        return ResponseEntity.ok()
            .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<Table> edit(
        @PathVariable Long orderTableId,
        @RequestBody @Valid TableEmptyEditRequest request
    ) {
        tableService.editEmpty(orderTableId, request);
        return ResponseEntity.noContent()
            .build();
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<Table> changeNumberOfGuests(
        @PathVariable Long orderTableId,
        @RequestBody @Valid TableGuestEditRequest request
    ) {
        tableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.noContent()
            .build();
    }
}
