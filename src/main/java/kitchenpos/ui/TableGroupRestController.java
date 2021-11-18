package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.ui.dto.tablegroup.TableGroupRequest;
import kitchenpos.ui.dto.tablegroup.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@Valid @RequestBody final TableGroupRequest request) {
        final TableGroupResponse response = tableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }
}
