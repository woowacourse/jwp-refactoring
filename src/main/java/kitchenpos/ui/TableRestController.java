package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.service.OrderTableDto;
import kitchenpos.ordertable.service.OrderTableMapper;
import kitchenpos.ordertable.service.OrderTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestController {

    private final OrderTableService orderTableService;
    private final OrderTableMapper orderTableMapper;

    public TableRestController(
        final OrderTableService orderTableService,
        final OrderTableMapper orderTableMapper
    ) {
        this.orderTableService = orderTableService;
        this.orderTableMapper = orderTableMapper;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableDto> create(@RequestBody final OrderTableDto orderTableDto) {
        final OrderTable created = orderTableService.create(orderTableMapper.toEntity(orderTableDto));
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(OrderTableDto.from(created))
            ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableDto>> list() {
        List<OrderTableDto> orderTableDtos = orderTableService.list()
                                                       .stream()
                                                       .map(OrderTableDto::from)
                                                       .collect(toList());

        return ResponseEntity.ok()
                             .body(orderTableDtos)
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableDto> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableDto orderTableDto
    ) {
        OrderTable orderTable = orderTableService.changeEmpty(orderTableId, orderTableMapper.toEntity(orderTableDto));
        return ResponseEntity.ok()
                             .body(OrderTableDto.from(orderTable))
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableDto> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableDto orderTableDto
    ) {
        OrderTable orderTable = orderTableService.changeNumberOfGuests(orderTableId, orderTableMapper.toEntity(orderTableDto));
        return ResponseEntity.ok()
                             .body(OrderTableDto.from(orderTable))
            ;
    }
}
