package kitchenpos.ui.table;

import kitchenpos.application.table.TableGroupService;
import kitchenpos.application.table.dto.response.TableGroupDto;
import kitchenpos.ui.table.dto.TableGroupsRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupDto> create(@RequestBody final TableGroupsRequestDto requestBody) {
        final TableGroupDto created = tableGroupService.create(requestBody.toCreateTableGroupDto());
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
