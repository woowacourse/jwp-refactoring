package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateRequest request) {
        final OrderTableResponse response = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PatchMapping(value = "/api/tables/{orderTableId}", params = "empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestParam final boolean empty
    ) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, empty));
    }

    @PatchMapping(value = "/api/tables/{orderTableId}", params = "numberOfGuests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestParam final int numberOfGuests
    ) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, numberOfGuests));
    }
}
