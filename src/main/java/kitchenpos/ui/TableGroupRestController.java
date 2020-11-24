package kitchenpos.ui;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(
        @Valid @RequestBody final TableGroupCreateRequest tableGroupCreateRequest) {
        final TableGroup created = tableGroupService.create(tableGroupCreateRequest.toRequestEntity());
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
            .body(TableGroupResponse.from(created));
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
            .build();
    }
}
