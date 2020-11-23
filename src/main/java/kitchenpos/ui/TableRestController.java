package kitchenpos.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.TableService;
import kitchenpos.dto.table.OrderTableFindAllResponses;
import kitchenpos.dto.table.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.table.OrderTableUpdateEmptyResponse;
import kitchenpos.dto.table.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.dto.table.OrderTableUpdateNumberOfGuestsResponse;
import kitchenpos.dto.tableGroup.OrderTableCreateRequest;
import kitchenpos.dto.tableGroup.OrderTableCreateResponse;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableCreateResponse> create(
        @RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTableCreateResponse created = tableService.create(orderTableCreateRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<OrderTableFindAllResponses> findAll() {
        return ResponseEntity.ok()
            .body(tableService.findAll());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableUpdateEmptyResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeEmpty(orderTableId, orderTableUpdateEmptyRequest));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableUpdateNumberOfGuestsResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableUpdateNumberOfGuestsRequest orderTableUpdateNumberOfGuestsRequest
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeNumberOfGuests(orderTableId, orderTableUpdateNumberOfGuestsRequest))
            ;
    }
}
