package kitchenpos.presentation;

import kitchenpos.application.OrderTableGroupService;
import kitchenpos.domain.OrderTableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final OrderTableGroupService orderTableGroupService;

    public TableGroupRestController(final OrderTableGroupService orderTableGroupService) {
        this.orderTableGroupService = orderTableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<OrderTableGroup> create(@RequestBody final TableGroupCreateRequest request) {
        final Long tableGroupId = orderTableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + tableGroupId);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        orderTableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
