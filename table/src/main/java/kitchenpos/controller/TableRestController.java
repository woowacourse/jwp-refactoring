package kitchenpos.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.dto.request.ChangeEmptyTableRequest;
import kitchenpos.dto.request.ChangeTableGuestRequest;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.service.TableService;
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
    public ResponseEntity<OrderTableResponse> create(@Valid @RequestBody CreateOrderTableRequest request) {
        OrderTableResponse response = tableService.create(request);
        URI uri = URI.create("/api/tables/" + response.getId());

        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> findAll() {
        List<OrderTableResponse> response = tableService.findAll();

        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable Long orderTableId,
            @Valid @RequestBody ChangeEmptyTableRequest request
    ) {
        OrderTableResponse response = tableService.changeEmpty(orderTableId, request);

        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @Valid @RequestBody ChangeTableGuestRequest request
    ) {
        OrderTableResponse response = tableService.changeNumberOfGuests(orderTableId, request);

        return ResponseEntity.ok()
                .body(response);
    }
}
