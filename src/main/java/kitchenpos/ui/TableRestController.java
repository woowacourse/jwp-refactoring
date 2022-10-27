package kitchenpos.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.OrderTableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;

@RestController
public class TableRestController {
    private final OrderTableService orderTableService;

    public TableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTable created = orderTableService.create(orderTableRequest);
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
        @RequestBody final boolean isEmpty
    ) {
        return ResponseEntity.ok()
            .body(orderTableService.changeEmpty(orderTableId, isEmpty));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final int numberOfGuests
    ) {
        return ResponseEntity.ok()
            .body(orderTableService.changeNumberOfGuests(orderTableId, numberOfGuests));
    }
}
