package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.dto.CreateMenuGroupResponse;
import kitchenpos.menu.application.dto.SearchMenuGroupResponse;
import kitchenpos.menu.ui.dto.CreateMenuGroupRequest;
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
    public ResponseEntity<CreateMenuGroupResponse> create(@RequestBody CreateMenuGroupRequest request) {
        CreateMenuGroupResponse response = menuGroupService.create(request.toCommand());
        URI uri = URI.create("/api/menu-groups/" + response.id());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<SearchMenuGroupResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list());
    }
}
