package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.application.dto.response.TableDto;
import kitchenpos.application.dto.request.UpdateGuestNumberDto;
import kitchenpos.ui.dto.EmptyTableRequestDto;
import kitchenpos.ui.dto.TableGuestNumberRequestDto;
import kitchenpos.ui.dto.TableRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {

    private final TableService tableService;

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<TableDto> create(@RequestBody final TableRequestDto requestBody) {
        final TableDto created = tableService.create(requestBody.toCreateTableDto());
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableDto>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableDto> changeEmpty(@PathVariable final Long orderTableId,
                                                @RequestBody final EmptyTableRequestDto requestBody) {
        return ResponseEntity.ok().body(tableService.changeEmpty(requestBody.toEmptyTableDto(orderTableId)));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableDto> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                         @RequestBody final TableGuestNumberRequestDto requestBody) {
        UpdateGuestNumberDto updateGuestNumberDto = requestBody.toUpdateGuestNumberDto(orderTableId);
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(updateGuestNumberDto));
    }
}
