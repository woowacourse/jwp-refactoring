package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
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
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupCreateRequest menuGroupCreateRequest) {
        final MenuGroup created = menuGroupService.create(menuGroupCreateRequest.toMenuGroup());
        final MenuGroupResponse menuGroupResponse = MenuGroupResponse.from(created);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(menuGroupResponse)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        final List<MenuGroup> menuGroups = menuGroupService.list();
        final List<MenuGroupResponse> menuGroupResponses = menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(menuGroupResponses)
                ;
    }
}
