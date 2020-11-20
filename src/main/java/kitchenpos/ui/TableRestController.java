package kitchenpos.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.TableService;
import kitchenpos.dto.NumberOfGuestsChangeRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableEmptyChangeRequest;
import kitchenpos.dto.TableResponse;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<TableResponse> create(@RequestBody TableCreateRequest tableCreateRequest) {
        TableResponse tableResponse = tableService.create(tableCreateRequest);
        return ResponseEntity.created(URI.create("/api/tables/" + tableResponse.getId())).body(tableResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(@PathVariable Long orderTableId,
        @RequestBody TableEmptyChangeRequest tableEmptyChangeRequest) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, tableEmptyChangeRequest));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
        @RequestBody final NumberOfGuestsChangeRequest numberOfGuestsChangeRequest) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, numberOfGuestsChangeRequest));
    }
}
