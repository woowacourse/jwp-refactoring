package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.table.application.TableService;
import kitchenpos.table.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.ui.dto.request.TableCreateRequest;
import kitchenpos.table.ui.dto.response.OrderTableChangeEmptyResponse;
import kitchenpos.table.ui.dto.response.OrderTableChangeNumberOfGuestsResponse;
import kitchenpos.table.ui.dto.response.TableCreateResponse;
import kitchenpos.table.ui.dto.response.TableFindAllResponse;
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
    public ResponseEntity<TableCreateResponse> create(@RequestBody final TableCreateRequest request) {
        final TableCreateResponse response = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableFindAllResponse>> list() {
        final List<TableFindAllResponse> responses = tableService.list();
        return ResponseEntity.ok()
                .body(responses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableChangeEmptyResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final OrderTableChangeEmptyRequest request) {
        final OrderTableChangeEmptyResponse response = tableService.changeEmpty(orderTableId, request);
        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableChangeNumberOfGuestsResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTableChangeNumberOfGuestsResponse response
                = tableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.ok()
                .body(response);
    }
}
