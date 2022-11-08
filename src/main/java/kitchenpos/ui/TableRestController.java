package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/tables")
    public ResponseEntity<OrderTableResponse> create(@Valid @RequestBody final OrderTableRequest request) {
        OrderTableResponse response = tableService.create(request);
        URI uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @Valid @RequestBody final OrderTableRequest request
    ) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @Valid @RequestBody final OrderTableRequest request
    ) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, request));
    }

    @PostMapping("/table-groups")
    public ResponseEntity<TableGroupResponse> group(@RequestBody final TableGroupRequest request) {
        TableGroupResponse response = tableService.group(request);
        URI uri = URI.create("/api/table-groups/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
