package kitchenpos.ui;

import kitchenpos.ordertablegroup.application.dto.OrderTableGroupCreateRequest;
import kitchenpos.ordertablegroup.application.OrderTableGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class OrderTableGroupRestController {
    private final OrderTableGroupService orderTableGroupService;

    public OrderTableGroupRestController(final OrderTableGroupService orderTableGroupService) {
        this.orderTableGroupService = orderTableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<Void> create(@RequestBody final OrderTableGroupCreateRequest request) {
        final Long id = orderTableGroupService.create(request);
        return ResponseEntity.created(URI.create("/api/table-groups/" + id)).build();
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        orderTableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
