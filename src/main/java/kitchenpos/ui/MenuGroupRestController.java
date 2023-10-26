package kitchenpos.ui;

import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.dto.menu.CreateMenuGroupRequest;
import kitchenpos.dto.menu.ListMenuGroupResponse;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final CreateMenuGroupRequest menuGroup) {
        System.out.println("menuGroup = " + menuGroup);
        final MenuGroupResponse created = menuGroupService.create(menuGroup);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<ListMenuGroupResponse> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list())
                ;
    }
}
