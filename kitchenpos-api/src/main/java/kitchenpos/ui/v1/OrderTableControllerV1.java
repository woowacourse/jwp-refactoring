package kitchenpos.ui.v1;

import java.net.URI;
import java.util.List;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.dto.OrderTableUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tables")
public class OrderTableControllerV1 {

    private final OrderTableService orderTableService;

    public OrderTableControllerV1(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableCreateRequest request) {
        var response = orderTableService.create(request);
        URI uri = URI.create("/api/v1/tables/" + response.getId());
        return ResponseEntity.created(uri)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> findAll() {
        return ResponseEntity.ok()
            .body(orderTableService.findAll());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable Long orderTableId,
        @RequestBody OrderTableUpdateRequest request
    ) {
        return ResponseEntity.ok()
            .body(orderTableService.changeEmpty(orderTableId, request.isEmpty()));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable Long orderTableId,
        @RequestBody OrderTableUpdateRequest request
    ) {
        return ResponseEntity.ok()
            .body(orderTableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests()));
    }
}
