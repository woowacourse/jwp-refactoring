package kitchenpos.order.controller;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.controller.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.controller.dto.OrderTableChangeNumberOfGuests;
import kitchenpos.ordertable.controller.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final OrderTableService orderTableService;

    public TableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTable created = orderTableService.create(orderTableCreateRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
                .body(orderTableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeEmptyRequest OrderTableChangeEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeEmpty(orderTableId, OrderTableChangeEmptyRequest))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeNumberOfGuests orderTableChangeNumberOfGuests
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeNumberOfGuests(orderTableId, orderTableChangeNumberOfGuests))
                ;
    }
}
