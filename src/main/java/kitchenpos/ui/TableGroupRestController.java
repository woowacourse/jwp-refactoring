package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.domain.TableGroup;
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
    public ResponseEntity<TableGroup> create(@RequestBody final TableGroupRequest tableGroupRequest) {
        final TableGroup createdTableGroup = tableGroupService.create(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + createdTableGroup.getId());
        return ResponseEntity.created(uri)
                .body(createdTableGroup)
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
