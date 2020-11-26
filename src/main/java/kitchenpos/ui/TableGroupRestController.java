package kitchenpos.ui;

import java.net.URI;
import javax.validation.Valid;
import kitchenpos.application.TableGroupService;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(
        @RequestBody @Valid final TableGroupRequest tableGroupRequest) {
        final TableGroupResponse createdTableGroup = tableGroupService.create(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + createdTableGroup.getId());

        return ResponseEntity.created(uri)
            .body(createdTableGroup);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);

        return ResponseEntity.noContent()
            .build();
    }
}
