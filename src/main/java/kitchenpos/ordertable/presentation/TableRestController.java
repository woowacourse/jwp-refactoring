package kitchenpos.ordertable.presentation;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.presentation.dto.EmptyChangeRequest;
import kitchenpos.ordertable.presentation.dto.GuestChangeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<Void> create() {
        final Long tableId = tableService.create();
        final URI uri = URI.create("/api/tables/" + tableId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final EmptyChangeRequest emptyChangeRequest
    ) {
        tableService.changeEmpty(orderTableId, emptyChangeRequest.isEmpty());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody @Valid final GuestChangeRequest guestChangeRequest
    ) {
        tableService.changeNumberOfGuests(orderTableId, guestChangeRequest.getNumberOfGuests());
        return ResponseEntity.ok().build();
    }
}
