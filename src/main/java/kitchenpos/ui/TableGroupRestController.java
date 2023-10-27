package kitchenpos.ui;

import java.net.URI;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.CreateTableGroupDto;
import kitchenpos.ui.dto.request.CreateTableGroupRequest;
import kitchenpos.ui.dto.response.CreateTableGroupResponse;
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
    public ResponseEntity<CreateTableGroupResponse> create(@RequestBody final CreateTableGroupRequest request) {
        final CreateTableGroupDto createTableGroupDto = tableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + createTableGroupDto.getId());
        final CreateTableGroupResponse response = new CreateTableGroupResponse(createTableGroupDto);

        return ResponseEntity.created(uri)
                             .body(response);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);

        return ResponseEntity.noContent()
                             .build();
    }
}
