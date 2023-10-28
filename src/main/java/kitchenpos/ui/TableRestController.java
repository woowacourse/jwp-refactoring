package kitchenpos.ui;

import kitchenpos.ordertable.service.OrderTableService;
import kitchenpos.ordertable.service.OrderTableDto;
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
    public ResponseEntity<OrderTableDto> create(@RequestBody final OrderTableDto orderTableDto) {
        final OrderTableDto created = orderTableService.create(orderTableDto);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableDto>> list() {
        return ResponseEntity.ok()
                .body(orderTableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableDto> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableDto orderTableDto
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeEmpty(orderTableId, orderTableDto))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableDto> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableDto orderTableDto
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeNumberOfGuests(orderTableId, orderTableDto))
                ;
    }
}
