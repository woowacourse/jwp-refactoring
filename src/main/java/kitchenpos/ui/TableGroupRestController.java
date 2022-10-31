package kitchenpos.ui;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody TableGroupRequest tableGroupRequest) {

        final TableGroup created = tableGroupService.create(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + created.getId());

        final TableGroupResponse tableGroupResponse = TableGroupResponse.from(created);

        return ResponseEntity.created(uri)
                .body(tableGroupResponse);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    @ResponseStatus(NO_CONTENT)
    public void ungroup(@PathVariable final Long tableGroupId) {

        tableGroupService.ungroup(tableGroupId);
    }
}
