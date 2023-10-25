package kitchenpos.ui;

import kitchenpos.domain.order.service.TableService;
import kitchenpos.domain.order.service.dto.OrderTableCreateRequest;
import kitchenpos.domain.order.service.dto.OrderTableResponse;
import kitchenpos.domain.order.service.dto.OrderTableUpdateGuestsRequest;
import kitchenpos.domain.order.service.dto.OrderTableUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateRequest request) {
        final OrderTableResponse created = tableService.create(request);
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

    @PatchMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<Void> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableUpdateRequest request
    ) {
        tableService.changeEmpty(orderTableId, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<Void> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableUpdateGuestsRequest request
    ) {
        tableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.noContent().build();
    }
}
