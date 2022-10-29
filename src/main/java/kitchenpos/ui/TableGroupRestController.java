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

    @PostMapping("/api/v2/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupCreationRequest tableGroupCreationRequest) {
        final TableGroupDto created = tableGroupService.create(TableGroupCreationDto.from(tableGroupCreationRequest));
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(TableGroupResponse.from(created));
    }


    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroupTable(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }
}
