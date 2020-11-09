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
import kitchenpos.application.command.ChangeNumberOfOrderTableGuestsCommand;
import kitchenpos.application.command.ChangeOrderTableEmptyCommand;
import kitchenpos.application.command.CreateOrderTableCommand;
import kitchenpos.application.response.OrderTableResponse;

@RequestMapping(API_TABLES)
@RestController
public class OrderTableRestController {
    public static final String API_TABLES = "/api/tables";

    private final OrderTableService orderTableService;

    public OrderTableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(
            @RequestBody @Valid final CreateOrderTableCommand command) {
        OrderTableResponse response = orderTableService.create(command);
        final URI uri = URI.create(API_TABLES + "/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(orderTableService.list())
                ;
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeOrderTableEmptyCommand command
    ) {
        OrderTableResponse response = orderTableService.changeEmpty(orderTableId,
                command);
        return ResponseEntity.ok()
                .body(response)
                ;
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final ChangeNumberOfOrderTableGuestsCommand command
    ) {
        OrderTableResponse response = orderTableService.changeNumberOfGuests(orderTableId,
                command);
        return ResponseEntity.ok()
                .body(response)
                ;
    }
}
