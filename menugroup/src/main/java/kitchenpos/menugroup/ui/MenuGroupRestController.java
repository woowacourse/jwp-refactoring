package kitchenpos.menugroup.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.application.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.application.response.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {

    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupCreateRequest);
        URI uri = URI.create("/api/menu-groups/" + menuGroupResponse.getId());
        return ResponseEntity.created(uri)
                .body(menuGroupResponse);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list());
    }
}
