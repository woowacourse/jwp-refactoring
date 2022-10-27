package kitchenpos.ui;

import kitchenpos.application.MenuGroupApiService;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class MenuGroupRestController {
    private final MenuGroupApiService menuGroupApiService;

    public MenuGroupRestController(final MenuGroupApiService menuGroupApiService) {
        this.menuGroupApiService = menuGroupApiService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupRequest request) {
        MenuGroupResponse created = menuGroupApiService.create(request);
        URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupApiService.list());
    }
}
