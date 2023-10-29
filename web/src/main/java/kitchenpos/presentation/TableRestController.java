package kitchenpos.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.application.dto.TableChangeEmptyDto;
import kitchenpos.ordertable.application.dto.TableChangeNumberOfGuestsDto;
import kitchenpos.ordertable.application.dto.TableCreateDto;
import kitchenpos.ordertable.application.dto.TableDto;
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
    public ResponseEntity<TableDto> create(@RequestBody final TableCreateDto request) {
        final TableDto response = tableService.create(request);

        final URI uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableDto>> list() {
        final List<TableDto> response = tableService.list();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableDto> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final TableChangeEmptyDto request
    ) {
        final TableDto response = tableService.changeEmpty(orderTableId, request);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableDto> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final TableChangeNumberOfGuestsDto request
    ) {
        final TableDto response = tableService.changeNumberOfGuests(orderTableId, request);

        return ResponseEntity.ok().body(response);
    }
}
