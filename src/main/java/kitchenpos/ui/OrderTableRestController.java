package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.ui.apiservice.OrderTabelApiService;
import kitchenpos.ui.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderTableRestController {

    private final OrderTabelApiService orderTabelApiService;

    public OrderTableRestController(final OrderTabelApiService orderTabelApiService) {
        this.orderTabelApiService = orderTabelApiService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest request) {
        final OrderTableResponse response = orderTabelApiService.create(request);
        final URI uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(orderTabelApiService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableChangeEmptyRequest request
    ) {
        return ResponseEntity.ok()
                .body(orderTabelApiService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId,
            @RequestBody OrderTableChangeNumberOfGuestsRequest request
    ) {
        return ResponseEntity.ok()
                .body(orderTabelApiService.changeNumberOfGuests(orderTableId, request));
    }
}
