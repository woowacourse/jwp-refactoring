package kitchenpos.ordertable.presentation;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.domain.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.domain.dto.request.OrderTableCreateRequest;
import kitchenpos.ordertable.domain.dto.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TableRestController {

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateRequest request) {
        final OrderTable created = tableService.create(request.toEntity());
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(OrderTableResponse.toDTO(created))
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> response = tableService.list().stream()
                .map(OrderTableResponse::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(response)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeEmptyRequest request
    ) {
        return ResponseEntity.ok()
                .body(OrderTableResponse.toDTO(
                        tableService.changeEmpty(orderTableId, request.isEmpty()))
                )
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeNumberOfGuestsRequest request
    ) {
        return ResponseEntity.ok()
                .body(OrderTableResponse.toDTO(tableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests())))
                ;
    }
}
