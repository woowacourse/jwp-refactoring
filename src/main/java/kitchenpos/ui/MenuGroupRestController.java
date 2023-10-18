package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/menu-groups")
@RestController
public class MenuGroupRestController {

    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list());
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupRequest menuGroupRequest) {
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);
        URI uri = URI.create("/api/menu-groups/" + menuGroupResponse.getId());
        return ResponseEntity.created(uri)
                .body(menuGroupResponse);
    }
}
