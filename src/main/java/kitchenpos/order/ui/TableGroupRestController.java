package kitchenpos.order.ui;

import java.net.URI;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.application.dto.TableGroupCreationDto;
import kitchenpos.order.application.dto.TableGroupDto;
import kitchenpos.order.ui.dto.request.TableGroupCreationRequest;
import kitchenpos.order.ui.dto.response.TableGroupResponse;
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

    @PostMapping("/api/v2/table-groups")
    public ResponseEntity<TableGroupResponse> create(
            @RequestBody final TableGroupCreationRequest tableGroupCreationRequest) {
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
