package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.TableService;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.ui.dto.request.CreateOrderTableRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableNumberOfGuestsRequest;
import kitchenpos.ui.dto.response.CreateOrderTableResponse;
import kitchenpos.ui.dto.response.ReadOrderTableResponse;
import kitchenpos.ui.dto.response.UpdateNumberOfGuestsResponse;
import kitchenpos.ui.dto.response.UpdateOrderTableEmptyResponse;
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
    public ResponseEntity<CreateOrderTableResponse> create(@RequestBody final CreateOrderTableRequest request) {
        final OrderTable created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        final CreateOrderTableResponse response = new CreateOrderTableResponse(created);

        return ResponseEntity.created(uri)
                             .body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<ReadOrderTableResponse>> list() {
        final List<ReadOrderTableResponse> responses = tableService.list()
                                                                   .stream()
                                                                   .map(ReadOrderTableResponse::new)
                                                                   .collect(Collectors.toList());

        return ResponseEntity.ok()
                             .body(responses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<UpdateOrderTableEmptyResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final UpdateOrderTableEmptyRequest request
    ) {
        final OrderTable updated = tableService.changeEmpty(orderTableId, request);
        final UpdateOrderTableEmptyResponse response = new UpdateOrderTableEmptyResponse(updated);

        return ResponseEntity.ok()
                             .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<UpdateNumberOfGuestsResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final UpdateOrderTableNumberOfGuestsRequest request
    ) {
        final OrderTable updated = tableService.changeNumberOfGuests(orderTableId, request);
        final UpdateNumberOfGuestsResponse response = new UpdateNumberOfGuestsResponse(updated);

        return ResponseEntity.ok()
                             .body(response);
    }
}
