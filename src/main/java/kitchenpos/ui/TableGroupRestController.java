package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroup> create(@RequestBody TableGroup tableGroup) {
        TableGroup created = tableGroupService.create(tableGroup);
        URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
