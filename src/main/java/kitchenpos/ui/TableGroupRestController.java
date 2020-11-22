package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
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
    public ResponseEntity<TableGroupResponse> respondCreatedTableGroupResponse(
            @RequestBody final TableGroupCreateRequest tableGroupCreateRequest
    ) {
        final TableGroupResponse created = tableGroupService.createTableGroup(tableGroupCreateRequest);
        final URI uri = URI.create("/api/table-groups/" + created.getId());

        return ResponseEntity.created(uri)
                .body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> respondUngroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);

        return ResponseEntity.noContent()
                .build();
    }
}
