package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.application.OrderTableService;
import kitchenpos.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.dto.request.OrderTableChangeGuestNumberRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestController {

    private final OrderTableService orderTableService;

    public TableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@Valid @RequestBody final OrderTableCreateRequest request) {
        final OrderTableResponse created = orderTableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(orderTableService.list());
    }

    @PutMapping("/api/tables/{tableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long tableId,
                                                          @Valid @RequestBody final OrderTableChangeEmptyRequest request) {
        return ResponseEntity.ok()
                .body(orderTableService.changeEmpty(tableId, request));
    }

    @PutMapping("/api/tables/{tableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long tableId,
                                                                   @Valid @RequestBody final OrderTableChangeGuestNumberRequest request) {
        return ResponseEntity.ok()
                .body(orderTableService.changeNumberOfGuests(tableId, request));
    }
}
