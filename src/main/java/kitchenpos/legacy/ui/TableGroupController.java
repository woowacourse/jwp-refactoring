package kitchenpos.legacy.ui;

import java.net.URI;
import kitchenpos.legacy.application.LegacyTableGroupService;
import kitchenpos.legacy.domain.TableGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/table-groups")
public class TableGroupController {

    private final LegacyTableGroupService tableGroupService;

    public TableGroupController(LegacyTableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroup> create(@RequestBody TableGroup tableGroup) {
        TableGroup created = tableGroupService.create(tableGroup);
        URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
            .build();
    }
}
