package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableCreateRequest;
import kitchenpos.ui.dto.OrderTableGuestChangeRequest;
import kitchenpos.ui.dto.OrderTableStatusChangeRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TableRestController {
    private final TableService tableService;

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(
        @Valid @RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTable created = tableService.create(orderTableCreateRequest.toRequestEntity());
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(OrderTableResponse.from(created));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        final List<OrderTableResponse> orderTableResponses = tableService.list()
            .stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok()
            .body(orderTableResponses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @Valid @RequestBody final OrderTableStatusChangeRequest orderTableStatusChangeRequest
    ) {
        OrderTable orderTable = tableService.changeEmpty(orderTableId, orderTableStatusChangeRequest.toRequestEntity());
        return ResponseEntity.ok()
            .body(OrderTableResponse.from(orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @Valid @RequestBody final OrderTableGuestChangeRequest orderTableGuestChangeRequest
    ) {
        OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId,
            orderTableGuestChangeRequest.toRequestEntity());
        return ResponseEntity.ok()
            .body(OrderTableResponse.from(orderTable));
    }
}
