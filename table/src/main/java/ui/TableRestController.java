package ui;

import dto.request.OrderTableCreateRequest;
import dto.request.OrderTableUpdateEmptyRequest;
import dto.request.OrderTableUpdateNumberOfGuestsRequest;
import dto.response.OrderTableResponse;
import java.net.URI;
import java.util.List;
import application.TableService;
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

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableCreateRequest request) {
        OrderTableResponse response = tableService.create(request);
        URI uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> readAll() {
        return ResponseEntity.ok().body(tableService.readAll());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> updateIsEmpty(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableUpdateEmptyRequest request
    ) {
        return ResponseEntity.ok().body(tableService.updateIsEmpty(orderTableId, request));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableUpdateNumberOfGuestsRequest request
    ) {
        return ResponseEntity.ok().body(tableService.updateNumberOfGuests(orderTableId, request));
    }
}
