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
    public ResponseEntity<OrderTableResponse> create(
            @RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);
        URI uri = URI.create("/api/tables/" + orderTableResponse.getId());
        return ResponseEntity.created(uri)
                .body(orderTableResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> orderTableResponses = tableService.list();
        return ResponseEntity.ok()
                .body(orderTableResponses);
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                          @RequestBody final EmptyOrderTableRequest emptyOrderTableRequest) {
        OrderTableResponse orderTableResponse = tableService.changeEmpty(orderTableId, emptyOrderTableRequest);
        return ResponseEntity.ok()
                .body(orderTableResponse);
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                   @RequestBody final ChangeGuestNumberRequest changeGuestNumberRequest) {
        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(orderTableId,
                changeGuestNumberRequest);
        return ResponseEntity.ok()
                .body(orderTableResponse);
    }
}
