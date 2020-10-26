package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.TableValidationGroup;
import kitchenpos.util.BindingResultValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableDto> create(
            @RequestBody @Validated(TableValidationGroup.create.class) final OrderTableRequest orderTableRequest,
            BindingResult bindingResult
    ) {
        BindingResultValidator.validate(bindingResult);
        final OrderTableDto created = tableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableDto>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableDto> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody @Validated(TableValidationGroup.changeEmpty.class) final OrderTableRequest orderTable,
            BindingResult bindingResult

    ) {
        BindingResultValidator.validate(bindingResult);
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableDto> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody @Validated(TableValidationGroup.changeNumberOfGuests.class) final OrderTableRequest orderTableRequest,
            BindingResult bindingResult
    ) {
        BindingResultValidator.validate(bindingResult);
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTableRequest));
    }
}
