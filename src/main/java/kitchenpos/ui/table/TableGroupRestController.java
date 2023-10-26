package kitchenpos.ui.table;

import kitchenpos.application.table.TableGroupService;
import kitchenpos.application.table.request.TableGroupCreateRequest;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.ui.table.response.TableGroupResponse;
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
    public ResponseEntity<TableGroupResponse> create(@RequestBody TableGroupCreateRequest request) {
        final TableGroup tableGroup = tableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + tableGroup.getId());
        return ResponseEntity.created(uri).body(TableGroupResponse.of(tableGroup));
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
