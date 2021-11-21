package kitchenpos.ui;

import java.net.URI;
import kitchenpos.application.TableGroupService;
import kitchenpos.ui.dto.request.table.TableGroupRequestDto;
import kitchenpos.ui.dto.response.table.TableGroupResponseDto;
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
    public ResponseEntity<TableGroupResponseDto> create(
        @RequestBody final TableGroupRequestDto tableGroupRequestDto
    ) {
        final TableGroupResponseDto responseDto = tableGroupService.create(tableGroupRequestDto);

        final URI uri = URI.create("/api/table-groups/" + responseDto.getId());
        return ResponseEntity.created(uri)
            .body(responseDto)
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
