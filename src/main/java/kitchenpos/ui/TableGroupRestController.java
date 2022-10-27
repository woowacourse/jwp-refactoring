package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/table-groups")
@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest tableGroup) {
        final TableGroupResponse response = tableGroupService.create(tableGroup);
        final URI uri = URI.create("/api/table-groups/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }
}
