package kitchenpos.presentation.ordertable;

import java.net.URI;
import javax.validation.Valid;
import kitchenpos.application.ordertable.TableGroupService;
import kitchenpos.application.ordertable.dto.TableGroupCreateRequest;
import kitchenpos.application.ordertable.dto.TableGroupResponse;
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
    public ResponseEntity<TableGroupResponse> create(@RequestBody @Valid final TableGroupCreateRequest request) {
        TableGroupResponse response = tableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
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