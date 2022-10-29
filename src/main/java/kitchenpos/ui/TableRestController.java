package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.application.dto.request.OrderTableCreateReqeust;
import kitchenpos.application.dto.response.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateReqeust orderTableCreateReqeust) {
        final OrderTableResponse created = tableService.create(orderTableCreateReqeust);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping(value = "/api/tables/{orderTableId}", params = "empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestParam final boolean empty
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, empty))
                ;
    }

    @PutMapping(value = "/api/tables/{orderTableId}", params = "numberOfGuests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestParam final int numberOfGuests
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, numberOfGuests))
                ;
    }
}
