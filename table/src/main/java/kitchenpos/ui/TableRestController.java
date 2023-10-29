package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ChangeNumberOfGuestsCommand;
import kitchenpos.application.ChangeTableEmptyCommand;
import kitchenpos.application.CreateTableCommand;
import kitchenpos.application.OrderTableDto;
import kitchenpos.application.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableDto> create(@RequestBody final CreateTableCommand request) {
        final OrderTableDto created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableDto>> list() {
        List<OrderTableDto> tableDtos = tableService.list().stream()
                .map(OrderTableDto::from)
                .collect(toList());
        return ResponseEntity.ok()
                .body(tableDtos)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableDto> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final PutTableEmptyRequest request
    ) {
        final var result = tableService.changeEmpty(new ChangeTableEmptyCommand(orderTableId, request.getEmpty()));
        return ResponseEntity.ok()
                .body(result)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableDto> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final PutNumberOfGuestsRequest request
    ) {
        final ChangeNumberOfGuestsCommand command = new ChangeNumberOfGuestsCommand(orderTableId, request.getNumberOfGuests());
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(command))
                ;
    }
}
