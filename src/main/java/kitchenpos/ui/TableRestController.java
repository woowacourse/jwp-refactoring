package kitchenpos.ui;

import java.util.Map;
import kitchenpos.application.TableService;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<Long> create(@RequestBody final Map<String, Object> parameter) {
        final Long created = tableService.create(
                Integer.parseInt(String.valueOf(parameter.get("numberOfGuests"))),
                Boolean.parseBoolean(String.valueOf(parameter.get("empty")))
        );
        final URI uri = URI.create("/api/tables/" + created);
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final Map<String, Object> parameter
    ) {
        final boolean empty = Boolean.parseBoolean(String.valueOf(parameter.get("empty")));
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, empty));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final Map<String, Object> parameter
    ) {
        final int numberOfGuests = Integer.parseInt(String.valueOf(parameter.get("numberOfGuests")));
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, numberOfGuests));
    }
}
