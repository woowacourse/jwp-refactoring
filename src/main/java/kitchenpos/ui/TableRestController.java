package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableEmptyChangeRequest;
import kitchenpos.dto.request.OrderTableNumberOfGuestChangeRequest;
import kitchenpos.dto.response.OrderTableResponse;
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

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(
            @RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTable created = tableService.create(orderTableCreateRequest.toOrderTable());
        final OrderTableResponse orderTableResponse = OrderTableResponse.from(created);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(orderTableResponse)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final List<OrderTableResponse> orderTableResponses = tableService.list()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(orderTableResponses)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableEmptyChangeRequest orderTableEmptyChangeRequest
    ) {
        final OrderTable orderTable = tableService.changeEmpty(orderTableId,
                orderTableEmptyChangeRequest.toOrderTable());
        return ResponseEntity.ok()
                .body(OrderTableResponse.from(orderTable))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableNumberOfGuestChangeRequest orderTableNumberOfGuestChangeRequest
    ) {
        final OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId,
                orderTableNumberOfGuestChangeRequest.toOrderTable());
        return ResponseEntity.ok()
                .body(OrderTableResponse.from(orderTable))
                ;
    }
}
