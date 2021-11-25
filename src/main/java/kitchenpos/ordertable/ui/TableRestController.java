package kitchenpos.ordertable.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.ordertable.service.OrderTableService;
import kitchenpos.ordertable.service.dto.OrderTableRequest;
import kitchenpos.ordertable.service.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/tables")
@RestController
public class TableRestController {

    private final OrderTableService orderTableServiceImpl;

    public TableRestController(final OrderTableService orderTableServiceImpl) {
        this.orderTableServiceImpl = orderTableServiceImpl;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTable) {
        final OrderTableResponse created = orderTableServiceImpl.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok(orderTableServiceImpl.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestParam final Boolean empty
    ) {
        return ResponseEntity.ok(orderTableServiceImpl.changeEmpty(orderTableId, empty));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestParam final Integer numberOfGuests
    ) {
        return ResponseEntity.ok(orderTableServiceImpl.changeNumberOfGuests(orderTableId, numberOfGuests));
    }
}
