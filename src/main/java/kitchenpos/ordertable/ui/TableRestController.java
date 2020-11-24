package kitchenpos.ordertable.ui;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.OrderTableEmptyChangeRequest;
import kitchenpos.ordertable.dto.OrderTableGuestsChangeRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final OrderTableService orderTableService;

    public TableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody @Valid final OrderTableCreateRequest request) {
        OrderTableResponse response = orderTableService.create(request);
        URI uri = URI.create("/api/tables/" + response.getId());

        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> response = orderTableService.list();

        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId, @RequestBody @Valid final OrderTableEmptyChangeRequest request) {
        OrderTableResponse response = orderTableService.changeEmpty(orderTableId, request);

        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId, @RequestBody @Valid final OrderTableGuestsChangeRequest request) {
        return ResponseEntity.ok()
                .body(orderTableService.changeNumberOfGuests(orderTableId, request));
    }
}
