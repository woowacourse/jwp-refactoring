package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.order.application.TableService;
import kitchenpos.order.application.dto.OrderTableCreationDto;
import kitchenpos.order.application.dto.OrderTableDto;
import kitchenpos.order.ui.dto.request.OrderTableCreationRequest;
import kitchenpos.order.ui.dto.request.TableChangeEmptyRequest;
import kitchenpos.order.ui.dto.request.TableChangeNumberOfGuestsRequest;
import kitchenpos.order.ui.dto.response.OrderTableResponse;
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

    @PostMapping("/api/v2/tables")
    public ResponseEntity<OrderTableResponse> createOrderTable(
            @RequestBody final OrderTableCreationRequest orderTableCreationRequest) {
        final OrderTableResponse created = OrderTableResponse.from(
                tableService.createOrderTable(OrderTableCreationDto.from(orderTableCreationRequest)));
        final URI uri = URI.create("/api/v2/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/v2/tables")
    public ResponseEntity<List<OrderTableDto>> getOrderTables() {
        return ResponseEntity.ok().body(tableService.getOrderTables());
    }

    @PutMapping("/api/v2/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmptyStatus(
            @PathVariable final Long orderTableId,
            @RequestBody final TableChangeEmptyRequest emptyRequest
    ) {
        return ResponseEntity.ok()
                .body(OrderTableResponse.from(tableService.changeEmpty(orderTableId, emptyRequest.getEmpty())));
    }

    @PutMapping("/api/v2/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final TableChangeNumberOfGuestsRequest numberOfGuestsRequest
    ) {
        return ResponseEntity.ok().body(OrderTableResponse.from(
                tableService.changeNumberOfGuests(orderTableId, numberOfGuestsRequest.getNumberOfGuests())));
    }
}
