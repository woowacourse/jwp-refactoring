package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import javax.validation.Valid;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody @Valid final TableGroupRequest tableGroupRequest) {
        final TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + tableGroupResponse.getId());
        return ResponseEntity.created(uri)
                .body(tableGroupResponse);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
