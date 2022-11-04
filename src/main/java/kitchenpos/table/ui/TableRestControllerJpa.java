package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.table.application.TableService;
import kitchenpos.table.ui.dto.ordertable.ChangeEmptyRequest;
import kitchenpos.table.ui.dto.ordertable.ChangeEmptyResponse;
import kitchenpos.table.ui.dto.ordertable.ChangeNumberOfGuestsRequest;
import kitchenpos.table.ui.dto.ordertable.ChangeNumberOfGuestsResponse;
import kitchenpos.table.ui.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.table.ui.dto.ordertable.OrderTableCreateResponse;
import kitchenpos.table.ui.dto.ordertable.OrderTableListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestControllerJpa {

    private final TableService tableService;

    public TableRestControllerJpa(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableCreateResponse> create(@RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTableCreateResponse orderTableCreateResponse = tableService.create(orderTableCreateRequest);
        final URI uri = URI.create("/api/tables/" + orderTableCreateResponse.getId());
        return ResponseEntity.created(uri).body(orderTableCreateResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableListResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<ChangeEmptyResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                           @RequestBody final ChangeEmptyRequest changeEmptyRequest) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, changeEmptyRequest));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<ChangeNumberOfGuestsResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                             @RequestBody final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestsRequest));
    }
}
