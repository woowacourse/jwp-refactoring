package kitchenpos.ui;

import java.net.URI;
import kitchenpos.ui.apiservice.TableGroupApiService;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupRestController {

    private final TableGroupApiService tableGroupApiService;

    public TableGroupRestController(TableGroupApiService tableGroupApiService) {
        this.tableGroupApiService = tableGroupApiService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody TableGroupRequest request) {
        final TableGroupResponse created = tableGroupApiService.create(request);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable Long tableGroupId) {
        tableGroupApiService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
