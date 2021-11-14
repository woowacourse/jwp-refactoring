package kitchenpos.ui;

import javax.validation.Valid;
import kitchenpos.application.TableGroupService;
import kitchenpos.ui.request.TableGroupRequest;
import kitchenpos.ui.response.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/table-groups")
public class TableGroupController {

    private final TableGroupService tableGroupService;

    public TableGroupController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroupResponse> create(@Valid @RequestBody final TableGroupRequest request) {
        final TableGroupResponse response = tableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + response.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);

        return ResponseEntity.noContent().build();
    }
}
