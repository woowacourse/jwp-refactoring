package kitchenpos.ui;

import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    @ResponseStatus(OK)
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest orderTableRequest) {

        final OrderTable created = tableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());

        final OrderTableResponse orderTableResponse = OrderTableResponse.from(created);

        return ResponseEntity.created(uri)
                .body(orderTableResponse);
    }

    @GetMapping("/api/tables")
    @ResponseStatus(OK)
    public List<OrderTableResponse> list() {

        final List<OrderTable> orderTables = tableService.list();

        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    @ResponseStatus(OK)
    public OrderTableResponse changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody OrderTableRequest orderTableRequest) {

        final OrderTable orderTable = tableService.changeEmpty(orderTableId, orderTableRequest);

        return OrderTableResponse.from(orderTable);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    @ResponseStatus(OK)
    public OrderTableResponse changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody OrderTableRequest orderTableRequest) {

        final OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

        return OrderTableResponse.from(orderTable);
    }
}
