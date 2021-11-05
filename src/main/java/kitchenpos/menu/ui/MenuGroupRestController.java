package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.ui.dto.MenuGroupRequest;
import kitchenpos.menu.ui.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody @Valid final MenuGroupRequest menuGroupRequest) {
        final MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);
        final URI uri = URI.create("/api/menu-groups/" + menuGroupResponse.getId());
        return ResponseEntity.created(uri)
                .body(menuGroupResponse)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list())
                ;
    }
}
