package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.table.OrderTableRequestDto;
import kitchenpos.ui.dto.response.table.OrderTableResponseDto;
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
    public ResponseEntity<OrderTableResponseDto> create(
        @RequestBody final OrderTableRequestDto orderTableRequestDto
    ) {
        final OrderTable created = tableService.create(new OrderTable(
            orderTableRequestDto.getNumberOfGuests(),
            orderTableRequestDto.getEmpty()
        ));

        OrderTableResponseDto responseDto = getOrderTableResponseDto(created);

        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(responseDto)
            ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponseDto>> list() {
        List<OrderTable> orderTables = tableService.list();
        List<OrderTableResponseDto> responseDtos = orderTables.stream()
            .map(this::getOrderTableResponseDto)
            .collect(toList());

        return ResponseEntity.ok()
            .body(responseDtos)
            ;
    }

    private OrderTableResponseDto getOrderTableResponseDto(OrderTable created) {
        return new OrderTableResponseDto(
            created.getId(),
            created.getTableGroupId(),
            created.getNumberOfGuests(),
            created.isEmpty()
        );
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponseDto> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableRequestDto orderTableRequestDto
    ) {
        OrderTable changed = tableService.changeEmpty(
            orderTableId, new OrderTable(orderTableRequestDto.getEmpty())
        );

        OrderTableResponseDto responseDto = new OrderTableResponseDto(
            changed.getId(),
            changed.getTableGroupId(),
            changed.getNumberOfGuests(),
            changed.isEmpty()
        );

        return ResponseEntity.ok()
            .body(responseDto)
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponseDto> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableRequestDto orderTableRequestDto
    ) {
        OrderTable changed = tableService.changeNumberOfGuests(
            orderTableId,
            new OrderTable(orderTableRequestDto.getNumberOfGuests())
        );

        OrderTableResponseDto responseDto = new OrderTableResponseDto(
            changed.getId(),
            changed.getTableGroupId(),
            changed.getNumberOfGuests(),
            changed.isEmpty()
        );

        return ResponseEntity.ok()
            .body(responseDto)
            ;
    }
}
