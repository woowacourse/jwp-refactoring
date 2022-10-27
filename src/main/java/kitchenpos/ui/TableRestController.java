package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.NumberOfGuestsChangeRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.request.TableEmptyChangeRequest;
import kitchenpos.ui.dto.response.OrderTableCreateResponse;
import kitchenpos.ui.dto.response.OrderTableResponse;
import kitchenpos.ui.dto.response.OrderTableUpdateResponse;
import kitchenpos.ui.mapper.OrderTableDtoMapper;
import kitchenpos.ui.mapper.OrderTableMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestController {

    private final OrderTableMapper orderTableMapper;
    private final OrderTableDtoMapper orderTableDtoMapper;
    private final TableService tableService;

    public TableRestController(final OrderTableMapper orderTableMapper, final OrderTableDtoMapper orderTableDtoMapper,
                               final TableService tableService) {
        this.orderTableMapper = orderTableMapper;
        this.orderTableDtoMapper = orderTableDtoMapper;
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableCreateResponse> create(
            @RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = orderTableMapper.toOrderTable(orderTableCreateRequest);
        OrderTableCreateResponse created = orderTableDtoMapper.toOrderTableCreateResponse(
                tableService.create(orderTable)
        );
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> orderTableResponses = orderTableDtoMapper.toOrderTableResponses(tableService.list());
        return ResponseEntity.ok().body(orderTableResponses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableUpdateResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                                @RequestBody final TableEmptyChangeRequest tableEmptyChangeRequest) {
        OrderTableUpdateResponse orderTableUpdateResponse = orderTableDtoMapper.toOrderTableUpdateResponse(
                tableService.changeEmpty(orderTableId, tableEmptyChangeRequest.isEmpty()));
        return ResponseEntity.ok().body(orderTableUpdateResponse);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableUpdateResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                         @RequestBody final NumberOfGuestsChangeRequest numberOfGuestsChangeRequest) {
        OrderTableUpdateResponse orderTableUpdateResponse = orderTableDtoMapper.toOrderTableUpdateResponse(
                tableService.changeNumberOfGuests(orderTableId, numberOfGuestsChangeRequest.getNumberOfGuests())
        );
        return ResponseEntity.ok().body(orderTableUpdateResponse);
    }
}
