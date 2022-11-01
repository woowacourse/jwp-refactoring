package kitchenpos.ui;

import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.request.MenuGroupRequest;
import kitchenpos.ui.response.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest request) {
        final MenuGroup created = menuGroupService.create(request.getName());
        final MenuGroupResponse response = new MenuGroupResponse(created.getId(), created.getName());
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        List<MenuGroupResponse> body = menuGroupService.list().stream()
                .map(m -> new MenuGroupResponse(m.getId(), m.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(body);
    }
}
