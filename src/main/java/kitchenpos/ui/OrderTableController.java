package kitchenpos.ui;

import javax.validation.Valid;
import kitchenpos.application.OrderTableService;
import kitchenpos.ui.request.OrderTableEmptyRequest;
import kitchenpos.ui.request.OrderTableGuestsRequest;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class OrderTableController {

    private final OrderTableService orderTableService;

    public OrderTableController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@Valid @RequestBody final OrderTableRequest request) {
        final OrderTableResponse response = orderTableService.create(request);
        final URI uri = URI.create("/api/tables/" + response.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(orderTableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @Valid @RequestBody final OrderTableEmptyRequest request
    ) {
        return ResponseEntity.ok().body(orderTableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @Valid @RequestBody final OrderTableGuestsRequest request
    ) {
        return ResponseEntity.ok().body(orderTableService.changeNumberOfGuests(orderTableId, request));
    }
}
