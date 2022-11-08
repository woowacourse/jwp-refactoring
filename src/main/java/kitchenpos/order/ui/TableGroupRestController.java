package kitchenpos.order.ui;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.ui.request.TableGroupRequest;
import kitchenpos.order.ui.request.TableIdRequest;
import kitchenpos.order.ui.response.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest request) {
        List<Long> orderTableIds = request.getOrderTables().stream()
                .map(TableIdRequest::getId)
                .collect(Collectors.toList());
        final TableGroup created = tableGroupService.create(orderTableIds);
        final TableGroupResponse body = tableGroupService.findById(created.getId());
        final URI uri = URI.create("/api/table-groups/" + body.getId());
        return ResponseEntity.created(uri).body(body);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
