package kitchenpos.ui.jpa;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableServiceJpa;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.jpa.dto.ordertable.ChangeEmptyRequest;
import kitchenpos.ui.jpa.dto.ordertable.ChangeEmptyResponse;
import kitchenpos.ui.jpa.dto.ordertable.ChangeNumberOfGuestsRequest;
import kitchenpos.ui.jpa.dto.ordertable.ChangeNumberOfGuestsResponse;
import kitchenpos.ui.jpa.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.ui.jpa.dto.ordertable.OrderTableCreateResponse;
import kitchenpos.ui.jpa.dto.ordertable.OrderTableListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestControllerJpa {

    private final TableServiceJpa tableService;

    public TableRestControllerJpa(final TableServiceJpa tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/new/api/tables")
    public ResponseEntity<OrderTableCreateResponse> create(@RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTableCreateResponse orderTableCreateResponse = tableService.create(orderTableCreateRequest);
        final URI uri = URI.create("/api/tables/" + orderTableCreateResponse.getId());
        return ResponseEntity.created(uri).body(orderTableCreateResponse);
    }

    @GetMapping("/new/api/tables")
    public ResponseEntity<List<OrderTableListResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/new/api/tables/{orderTableId}/empty")
    public ResponseEntity<ChangeEmptyResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                           @RequestBody final ChangeEmptyRequest changeEmptyRequest) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, changeEmptyRequest));
    }

    @PutMapping("/new/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<ChangeNumberOfGuestsResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                             @RequestBody final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestsRequest));
    }
}
