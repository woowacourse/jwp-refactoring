package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.ui.dto.request.table.OrderTableEmptyRequestDto;
import kitchenpos.ui.dto.request.table.OrderTableGuestRequestDto;
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
        OrderTableResponseDto responseDto = tableService.create(orderTableRequestDto);

        final URI uri = URI.create("/api/tables/" + responseDto.getId());
        return ResponseEntity.created(uri)
            .body(responseDto)
            ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponseDto>> list() {
        List<OrderTableResponseDto> responseDtos = tableService.list();

        return ResponseEntity.ok()
            .body(responseDtos)
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponseDto> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableEmptyRequestDto orderTableEmptyRequestDto
    ) {
        OrderTableResponseDto responseDto = tableService
            .changeEmpty(orderTableId, orderTableEmptyRequestDto);

        return ResponseEntity.ok()
            .body(responseDto)
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponseDto> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableGuestRequestDto orderTableGuestRequestDto
    ) {
        OrderTableResponseDto responseDto = tableService.changeNumberOfGuests(
            orderTableId,
            orderTableGuestRequestDto
        );

        return ResponseEntity.ok()
            .body(responseDto)
            ;
    }
}
