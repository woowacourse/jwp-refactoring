package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.application.dto.CreateOrderTableDto;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.UpdateOrderTableEmptyDto;
import kitchenpos.application.dto.UpdateOrderTableGuestNumberDto;
import kitchenpos.ui.request.UpdateOrderTableEmptyRequest;
import kitchenpos.ui.request.UpdateOrderTableGuestNumberRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {

    private final TableService tableService;

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableDto> create(@RequestBody CreateOrderTableDto request) {
        OrderTableDto created = tableService.create(request);
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableDto>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableDto> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody UpdateOrderTableEmptyRequest request) {
        UpdateOrderTableEmptyDto updateOrderTableEmptyDto
                = new UpdateOrderTableEmptyDto(orderTableId, request.getEmpty());

        OrderTableDto updated = tableService.changeEmpty(updateOrderTableEmptyDto);
        return ResponseEntity.ok().body(updated);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableDto> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody UpdateOrderTableGuestNumberRequest request) {
        UpdateOrderTableGuestNumberDto updateOrderTableGuestNumberDto
                = new UpdateOrderTableGuestNumberDto(orderTableId, request.getNumberOfGuests());

        OrderTableDto updated = tableService.changeNumberOfGuests(updateOrderTableGuestNumberDto);
        return ResponseEntity.ok().body(updated);
    }
}
