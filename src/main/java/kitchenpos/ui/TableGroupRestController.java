package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.TableGroupCreationDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.TableGroupCreationRequest;
import kitchenpos.ui.dto.response.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @Deprecated
    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroup> create(@RequestBody final TableGroup tableGroup) {
        final TableGroup created = tableGroupService.create(tableGroup);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @PostMapping("/api/v2/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupCreationRequest tableGroupCreationRequest) {
        final TableGroupDto created = tableGroupService.create(TableGroupCreationDto.from(tableGroupCreationRequest));
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(TableGroupResponse.from(created));
    }


    @Deprecated
    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }
}
