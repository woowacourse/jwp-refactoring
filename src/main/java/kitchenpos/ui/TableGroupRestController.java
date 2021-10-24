package kitchenpos.ui;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupRestController {
//    private final TableGroupService tableGroupService;
//
//    public TableGroupRestController(final TableGroupService tableGroupService) {
//        this.tableGroupService = tableGroupService;
//    }
//
//    @PostMapping("/api/table-groups")
//    public ResponseEntity<TableGroup> create(@RequestBody final TableGroup tableGroup) {
//        final TableGroup created = tableGroupService.create(tableGroup);
//        final URI uri = URI.create("/api/table-groups/" + created.getId());
//        return ResponseEntity.created(uri)
//                .body(created)
//                ;
//    }
//
//    @DeleteMapping("/api/table-groups/{tableGroupId}")
//    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
//        tableGroupService.ungroup(tableGroupId);
//        return ResponseEntity.noContent()
//                .build()
//                ;
//    }
}
