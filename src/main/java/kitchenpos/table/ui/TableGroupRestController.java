package kitchenpos.table.ui;

import java.net.URI;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.dto.request.TableGroupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<Void> create(@RequestBody TableGroupRequest tableGroupRequest) {
        Long tableGroupId = tableGroupService.create(tableGroupRequest);
        URI uri = URI.create("/api/table-groups/" + tableGroupId);
        return ResponseEntity.created(uri)
                .build();
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
