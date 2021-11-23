package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.ui.dto.OrderTableAssembler;
import kitchenpos.ui.dto.request.OrderTableRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableService tableService;

    public TableController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest request) {
        OrderTableResponse response = OrderTableAssembler.orderTableResponse(
            tableService.create(OrderTableAssembler.orderTableRequestDto(request)));
        URI uri = URI.create("/api/tables/" + response.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> responses = OrderTableAssembler
            .orderTableResponses(tableService.list());

        return ResponseEntity.ok().body(responses);
    }

//    @PutMapping("/{orderTableId}/empty")
//    public ResponseEntity<OrderTable> changeEmpty(
//        @PathVariable final Long orderTableId,
//        @RequestBody final OrderTable orderTable
//    ) {
//        return ResponseEntity.ok().body(
//            tableService.changeEmpty(orderTableId, orderTable)
//        );
//    }
//
//    @PutMapping("/{orderTableId}/number-of-guests")
//    public ResponseEntity<OrderTable> changeNumberOfGuests(
//        @PathVariable final Long orderTableId,
//        @RequestBody final OrderTable orderTable
//    ) {
//        return ResponseEntity.ok().body(
//            tableService.changeNumberOfGuests(orderTableId, orderTable)
//        );
//    }
}
