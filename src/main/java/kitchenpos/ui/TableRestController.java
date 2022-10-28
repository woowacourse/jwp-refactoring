package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest request) {
        final OrderTableResponse created = tableService.create(request.toCommand());
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

//    @PutMapping("/api/tables/{orderTableId}/empty")
//    public ResponseEntity<OrderTable> changeEmpty(
//            @PathVariable final Long orderTableId,
//            @RequestBody final OrderTable orderTable
//    ) {
//        return ResponseEntity.ok()
//                .body(tableService.changeEmpty(orderTableId, orderTable))
//                ;
//    }
//
//    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
//    public ResponseEntity<OrderTable> changeNumberOfGuests(
//            @PathVariable final Long orderTableId,
//            @RequestBody final OrderTable orderTable
//    ) {
//        return ResponseEntity.ok()
//                .body(tableService.changeNumberOfGuests(orderTableId, orderTable))
//                ;
//    }
}
