package kitchenpos.ordertable.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.model.OrderTable;
import kitchenpos.ordertable.application.dto.OrderTableResponseDto;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponseDto> create() {
        final OrderTableResponseDto orderTableResponse = tableService.create();
        final URI uri = URI.create("/api/tables/" + orderTableResponse.getId());
        return ResponseEntity.created(uri)
            .body(orderTableResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponseDto>> list() {
        return ResponseEntity.ok()
            .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponseDto> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final boolean isEmpty
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeEmpty(orderTableId, isEmpty));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponseDto> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTable orderTable
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeNumberOfGuests(orderTableId, orderTable.getNumberOfGuests()));
    }
}
