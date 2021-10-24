package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.request.OrderTableChangeRequestDto;
import kitchenpos.application.dto.request.OrderTableCreateRequestDto;
import kitchenpos.application.dto.response.OrderTableResponseDto;
import kitchenpos.ui.dto.request.OrderTableRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
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

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest orderTableRequest) {
        OrderTableCreateRequestDto orderTableCreateRequestDto = orderTableRequest.toDto();
        OrderTableResponseDto orderTableResponseDto = tableService.create(orderTableCreateRequestDto);
        OrderTableResponse orderTableResponse = OrderTableResponse.from(orderTableResponseDto);
        URI uri = URI.create("/api/tables/" + orderTableResponse.getId());
        return ResponseEntity.created(uri)
            .body(orderTableResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> orderTableResponses = tableService.list()
            .stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok()
            .body(orderTableResponses);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable Long orderTableId,
        @RequestBody OrderTableRequest orderTableRequest
    ) {
        OrderTableChangeRequestDto orderTableChangeRequestDto =
            new OrderTableChangeRequestDto(orderTableId, orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        OrderTableResponseDto orderTableResponseDto = tableService.changeEmpty(orderTableChangeRequestDto);
        OrderTableResponse orderTableResponse = OrderTableResponse.from(orderTableResponseDto);
        return ResponseEntity.ok()
            .body(orderTableResponse);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable Long orderTableId,
        @RequestBody OrderTableRequest orderTableRequest
    ) {
        OrderTableChangeRequestDto orderTableChangeRequestDto =
            new OrderTableChangeRequestDto(orderTableId, orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        OrderTableResponseDto orderTableResponseDto = tableService.changeNumberOfGuests(orderTableChangeRequestDto);
        OrderTableResponse orderTableResponse = OrderTableResponse.from(orderTableResponseDto);
        return ResponseEntity.ok()
            .body(orderTableResponse);
    }
}
