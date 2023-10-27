package kitchenpos.menugroup.ui;

import kitchenpos.menugroup.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.response.MenuGroupResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupCreateRequest request) {
        MenuGroup created = menuGroupService.create(request);
        URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri).body(MenuGroupResponse.from(created));
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        List<MenuGroupResponse> response = menuGroupService.list().stream()
                .map(menuGroup -> new MenuGroupResponse(menuGroup.getId(), menuGroup.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }
}
