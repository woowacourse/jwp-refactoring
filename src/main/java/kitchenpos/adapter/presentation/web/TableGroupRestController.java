package kitchenpos.adapter.presentation.web;

import static kitchenpos.adapter.presentation.web.TableGroupRestController.*;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.command.CreateTableGroupCommand;
import kitchenpos.application.response.TableGroupResponse;

@RequestMapping(API_TABLE_GROUPS)
@RestController
public class TableGroupRestController {
    public static final String API_TABLE_GROUPS = "/api/table-groups";

    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroupResponse> create(
            @RequestBody final CreateTableGroupCommand command) {
        TableGroupResponse response = tableGroupService.create(command);
        final URI uri = URI.create(API_TABLE_GROUPS + "/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }
}
