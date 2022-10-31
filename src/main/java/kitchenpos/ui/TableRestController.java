package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.dto.TableChangeEmptyRequest;
import kitchenpos.dto.TableChangeEmptyResponse;
import kitchenpos.dto.TableChangeNumberOfGuestsRequest;
import kitchenpos.dto.TableChangeNumberOfGuestsResponse;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableCreateResponse;
import kitchenpos.dto.TableFindResponse;
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

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<TableCreateResponse> create(@RequestBody final TableCreateRequest tableCreateRequest) {
        final TableCreateResponse created =
                tableService.create(tableCreateRequest.getNumberOfGuests(), tableCreateRequest.isEmpty());
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableFindResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableChangeEmptyResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final TableChangeEmptyRequest tableChangeEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, tableChangeEmptyRequest.isEmpty()))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableChangeNumberOfGuestsResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final TableChangeNumberOfGuestsRequest tableChangeNumberOfGuestsRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(
                        orderTableId,
                        tableChangeNumberOfGuestsRequest.getNumberOfGuests())
                )
                ;
    }
}
