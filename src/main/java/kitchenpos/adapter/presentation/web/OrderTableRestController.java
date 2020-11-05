package kitchenpos.adapter.presentation.web;

import static kitchenpos.adapter.presentation.web.OrderTableRestController.*;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.OrderTableService;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuests;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;

@RequestMapping(API_TABLES)
@RestController
public class OrderTableRestController {
    public static final String API_TABLES = "/api/tables";

    private final OrderTableService orderTableService;

    public OrderTableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final OrderTableCreateRequest request) {
        Long tableId = orderTableService.create(request);
        final URI uri = URI.create(API_TABLES + "/" + tableId);
        return ResponseEntity.created(uri)
                .build()
                ;
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(orderTableService.list())
                ;
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<Void> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeEmptyRequest request
    ) {
        orderTableService.changeEmpty(orderTableId, request);
        return ResponseEntity.ok()
                .build()
                ;
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<Void> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final OrderTableChangeNumberOfGuests request
    ) {
        orderTableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.ok()
                .build()
                ;
    }
}
