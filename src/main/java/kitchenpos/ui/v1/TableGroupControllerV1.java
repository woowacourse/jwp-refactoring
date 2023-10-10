package kitchenpos.ui.v1;

import java.net.URI;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/table-groups")
public class TableGroupControllerV1 {

    private final TableGroupService tableGroupService;

    public TableGroupControllerV1(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroup> create(@RequestBody TableGroup tableGroup) {
        TableGroup created = tableGroupService.create(tableGroup);
        URI uri = URI.create("/api/v1/table-groups/" + created.getId());
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
