package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeGuestNumberRequest;
import kitchenpos.table.dto.OrderTableEmptyResponse;
import kitchenpos.table.dto.OrderTableNumberOfGuestResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@RestController
public class TableRestController {

    private final OrderTableService orderTableService;

    public TableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTable created = orderTableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());

        return ResponseEntity.created(uri).body(OrderTableResponse.from(created));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTable> orderTables = orderTableService.list();

        List<OrderTableResponse> orderTableResponses = orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(orderTableResponses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableEmptyResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final ChangeEmptyRequest changeEmptyRequest
    ) {
        OrderTable orderTable = orderTableService.changeEmpty(orderTableId, changeEmptyRequest);

        return ResponseEntity.ok().body(OrderTableEmptyResponse.from(orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableNumberOfGuestResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final ChangeGuestNumberRequest changeGuestNumberRequest
    ) {
        OrderTable orderTable = orderTableService.changeNumberOfGuests(orderTableId, changeGuestNumberRequest);

        return ResponseEntity.ok().body(OrderTableNumberOfGuestResponse.from(orderTable));
    }
}
