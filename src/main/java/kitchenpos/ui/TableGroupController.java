//package kitchenpos.ui;
//
//import java.net.URI;
//import kitchenpos.application.TableGroupService;
//import kitchenpos.domain.TableGroup;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/table-groups")
//public class TableGroupController {
//
//    private final TableGroupService tableGroupService;
//
//    public TableGroupController(final TableGroupService tableGroupService) {
//        this.tableGroupService = tableGroupService;
//    }
//
//    @PostMapping
//    public ResponseEntity<TableGroup> create(@RequestBody final TableGroup tableGroup) {
//        final TableGroup created = tableGroupService.create(tableGroup);
//        final URI uri = URI.create("/api/table-groups/" + created.getId());
//
//        return ResponseEntity.created(uri).body(created);
//    }
//
//    @DeleteMapping("/{tableGroupId}")
//    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
//        tableGroupService.ungroup(tableGroupId);
//        return ResponseEntity.noContent().build();
//    }
//}
