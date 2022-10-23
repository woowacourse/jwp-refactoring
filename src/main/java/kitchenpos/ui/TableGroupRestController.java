package kitchenpos.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.TableGroupService;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody TableGroupRequest tableGroupRequest) {
        TableGroupResponse created = tableGroupService.create(tableGroupRequest);
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
