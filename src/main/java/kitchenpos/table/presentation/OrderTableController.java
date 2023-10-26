package kitchenpos.table.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.presentation.dto.request.ChangeEmptyRequest;
import kitchenpos.table.presentation.dto.request.ChangeNumberOfGuestsRequest;
import kitchenpos.table.presentation.dto.request.CreateOrderTableRequest;
import kitchenpos.table.presentation.dto.response.OrderTableResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/tables")
@RestController
public class OrderTableController {

    private final OrderTableService orderTableService;

    public OrderTableController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> createOrderTable(@RequestBody final CreateOrderTableRequest request) {
        final OrderTable orderTable = orderTableService.create(request);
        final OrderTableResponse response = OrderTableResponse.from(orderTable);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/api/tables/" + orderTable.getId()))
                             .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> findAllOrderTable() {
        final List<OrderTable> orderTables = orderTableService.list();
        final List<OrderTableResponse> responses = OrderTableResponse.convertToList(orderTables);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                          @RequestBody final ChangeEmptyRequest request) {
        final OrderTable orderTable = orderTableService.changeEmpty(orderTableId, request);
        final OrderTableResponse response = OrderTableResponse.from(orderTable);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                   @RequestBody final ChangeNumberOfGuestsRequest request) {
        final OrderTable orderTable = orderTableService.changeNumberOfGuests(orderTableId, request);
        final OrderTableResponse response = OrderTableResponse.from(orderTable);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }
}
