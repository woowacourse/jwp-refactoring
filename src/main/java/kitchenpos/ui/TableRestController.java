package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.table.OrderTable;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.request.OrderTableCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tables")
public class TableRestController {

    private final OrderTableService orderTableService;

    public TableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping
    public ResponseEntity<OrderTable> create(@RequestBody OrderTableCreateRequest request) {
        OrderTable created = orderTableService.create(request);
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok().body(orderTableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(@PathVariable Long orderTableId, @RequestBody boolean changedStatus) {
        return ResponseEntity.ok().body(orderTableService.changeEmpty(orderTableId, changedStatus));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(@PathVariable Long orderTableId,
                                                           @RequestBody int numberOfGuests) {
        return ResponseEntity.ok().body(orderTableService.changeNumberOfGuests(orderTableId, numberOfGuests));
    }
}
