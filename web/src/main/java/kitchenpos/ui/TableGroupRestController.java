package kitchenpos.ui;

import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.service.TableGroupMapper;
import kitchenpos.ordertable.service.TableGroupService;
import kitchenpos.ordertable.service.TableGroupDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;
    private final TableGroupMapper tableGroupMapper;

    public TableGroupRestController(
        final TableGroupService tableGroupService,
        final TableGroupMapper tableGroupMapper
    ) {
        this.tableGroupService = tableGroupService;
        this.tableGroupMapper = tableGroupMapper;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupDto> create(@RequestBody final TableGroupDto tableGroupDto) {
        final TableGroup created = tableGroupService.create(tableGroupMapper.toEntity(tableGroupDto));
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(TableGroupDto.from(created))
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
