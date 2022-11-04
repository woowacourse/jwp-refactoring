package kitchenpos.ui.group;

import java.net.URI;
import kitchenpos.application.group.TableGroupService;
import kitchenpos.dto.group.request.TableGroupCreateRequest;
import kitchenpos.dto.group.response.TableGroupResponse;
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
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupCreateRequest request) {
        TableGroupResponse response = tableGroupService.create(request);
        URI uri = URI.create("/api/table-groups/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
