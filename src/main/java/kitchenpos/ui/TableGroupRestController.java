package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.request.TableGroupsCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.TableGroup;
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
