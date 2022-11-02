package kitchenpos.ui.jpa;

import java.net.URI;
import kitchenpos.application.TableGroupServiceJpa;
import kitchenpos.ui.jpa.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.ui.jpa.dto.tablegroup.TableGroupCreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupRestControllerJpa {

    private final TableGroupServiceJpa tableGroupService;

    public TableGroupRestControllerJpa(final TableGroupServiceJpa tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/new/api/table-groups")
    public ResponseEntity<TableGroupCreateResponse> create(@RequestBody final TableGroupCreateRequest tableGroupCreateRequest) {
        TableGroupCreateResponse tableGroupCreateResponse = tableGroupService.create(tableGroupCreateRequest);
        final URI uri = URI.create("/api/table-groups/" + tableGroupCreateResponse.getId());
        return ResponseEntity.created(uri).body(tableGroupCreateResponse);
    }

    @DeleteMapping("/new/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
