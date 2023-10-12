package kitchenpos.ui.tablegroup;

import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.application.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.tablegroup.dto.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupCreateRequest tableGroupCreateRequest) {
        TableGroup tableGroup = tableGroupService.create(tableGroupCreateRequest);
        URI uri = URI.create("/api/table-groups/" + tableGroup.getId());

        return ResponseEntity.created(uri)
                .body(TableGroupResponse.from(tableGroup));
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);

        return ResponseEntity.noContent()
                .build();
    }
}
