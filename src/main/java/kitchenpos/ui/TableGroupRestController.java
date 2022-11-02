package kitchenpos.ui;

import kitchenpos.table.application.TableGroupService;
import kitchenpos.dto.request.TableGroupsCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.table.domain.TableGroup;
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
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupsCreateRequest tableGroupsCreateRequest) {
        final TableGroup created = tableGroupService.create(tableGroupsCreateRequest);
        final TableGroupResponse response = TableGroupResponse.from(created);
        final URI uri = URI.create("/api/table-groups/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
