package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.dto.request.ChangeGuestNumberRequest;
import kitchenpos.dto.request.EmptyOrderTableRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tables")
public class TableRestController {

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableCreateRequest request) {
        OrderTableResponse response = tableService.create(request);
        URI uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> responses = tableService.list();
        return ResponseEntity.ok()
                .body(responses);
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                          @RequestBody final EmptyOrderTableRequest request) {
        OrderTableResponse response = tableService.changeEmpty(orderTableId, request);
        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                   @RequestBody final ChangeGuestNumberRequest request) {
        OrderTableResponse response = tableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.ok()
                .body(response);
    }
}
