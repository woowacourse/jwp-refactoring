package kitchenpos.menuGroup.presentation;

import kitchenpos.menuGroup.application.MenuGroupService;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.application.dto.MenuGroupRequest;
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

    public MenuGroupRestController(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroup> create(@RequestBody MenuGroupRequest request) {
        MenuGroup menuGroup = menuGroupService.create(request);
        URI uri = URI.create("/api/menu-groups/" + menuGroup.getId());
        return ResponseEntity.created(uri)
                .body(menuGroup)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroup>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list())
                ;
    }
}
