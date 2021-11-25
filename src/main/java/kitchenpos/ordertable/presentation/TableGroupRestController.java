package kitchenpos.ordertable.presentation;

import kitchenpos.ordertable.application.TableGroupService;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.dto.request.TableGroupCreateRequest;
import kitchenpos.ordertable.domain.dto.response.TableGroupResponse;
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
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupCreateRequest request) {
        final TableGroup created = tableGroupService.create(request.getOrderTableIds());
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(TableGroupResponse.toDTO(created))
                ;
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }
}
