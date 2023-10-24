package kitchenpos.tablegroup.ui;

import java.net.URI;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/table-groups")
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest tableGroupRequest) {
        final TableGroupResponse created = tableGroupService.group(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + created.getId());

        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);

        return ResponseEntity.noContent().build();
    }
}
