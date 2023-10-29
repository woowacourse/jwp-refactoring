package kitchenpos.tablegroup.presentation;

import java.net.URI;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.presentation.dto.request.CreateTableGroupRequest;
import kitchenpos.tablegroup.presentation.dto.response.TableGroupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/table-groups")
@RestController
public class TableGroupController {

    private final TableGroupService tableGroupService;

    public TableGroupController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroupResponse> createTableGroup(final CreateTableGroupRequest request) {
        final TableGroup tableGroup = tableGroupService.create(request);
        final TableGroupResponse response = TableGroupResponse.from(tableGroup);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/api/table-groups/" + tableGroup.getId()))
                             .body(response);
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroupTable(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
