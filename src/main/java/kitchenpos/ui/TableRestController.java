package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.OrderTableCreationDto;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableCreationRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
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

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @Deprecated
    @PostMapping("/api/tables")
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTable orderTable) {
        final OrderTable created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @PostMapping("/api/v2/tables")
    public ResponseEntity<OrderTableResponse> createOrderTable(
            @RequestBody final OrderTableCreationRequest orderTableCreationRequest) {
        final OrderTableResponse created = OrderTableResponse.from(
                tableService.createOrderTable(OrderTableCreationDto.from(orderTableCreationRequest)));
        final URI uri = URI.create("/api/v2/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @Deprecated
    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @GetMapping("/api/v2/tables")
    public ResponseEntity<List<OrderTableDto>> getOrderTables() {
        return ResponseEntity.ok().body(tableService.getOrderTables());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTable orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTable))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTable orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTable))
                ;
    }
}
