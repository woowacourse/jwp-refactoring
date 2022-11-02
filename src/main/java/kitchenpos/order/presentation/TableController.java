package kitchenpos.order.presentation;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.util.List;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.presentation.dto.request.OrderTableRequest;
import kitchenpos.order.presentation.dto.response.TableChangeEmptyResponse;
import kitchenpos.order.presentation.dto.response.TableChangeGuestNumberResponse;
import kitchenpos.order.presentation.dto.response.TableCreateResponse;
import kitchenpos.order.presentation.dto.response.TableListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableController {
    private final TableService tableService;

    public TableController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    @ResponseStatus(OK)
    public ResponseEntity<TableCreateResponse> create(@RequestBody OrderTableRequest orderTableRequest) {

        final OrderTable created = tableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());

        return ResponseEntity.created(uri)
                .body(new TableCreateResponse(created));
    }

    @GetMapping("/api/tables")
    @ResponseStatus(OK)
    public List<TableListResponse> list() {

        final List<OrderTable> orderTables = tableService.list();

        return orderTables.stream()
                .map(TableListResponse::new)
                .collect(toList());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    @ResponseStatus(OK)
    public TableChangeEmptyResponse changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody OrderTableRequest orderTableRequest) {

        final OrderTable orderTable = tableService.changeEmpty(orderTableId, orderTableRequest);

        return new TableChangeEmptyResponse(orderTable);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    @ResponseStatus(OK)
    public TableChangeGuestNumberResponse changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody OrderTableRequest orderTableRequest) {

        OrderTable table = tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

        return new TableChangeGuestNumberResponse(table);
    }
}
