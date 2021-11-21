package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dtos.MenuGroupRequest;
import kitchenpos.application.dtos.MenuGroupResponse;
import kitchenpos.application.dtos.MenuGroupResponses;
import kitchenpos.domain.MenuGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest request) {
        final MenuGroupResponse response = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponses> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list());
    }
}
