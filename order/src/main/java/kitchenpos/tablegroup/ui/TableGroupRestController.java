package kitchenpos.tablegroup.ui;

import java.net.URI;
import javax.validation.Valid;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.ui.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.ui.response.TableGroupResponse;
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
            @Valid @RequestBody final TableGroupCreateRequest request) {
        final var response = tableGroupService.create(request);
        final var uri = URI.create("/api/table-groups/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
