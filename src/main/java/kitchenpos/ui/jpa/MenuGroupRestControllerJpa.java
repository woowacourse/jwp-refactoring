package kitchenpos.ui.jpa;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuGroupServiceJpa;
import kitchenpos.ui.jpa.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.ui.jpa.dto.menugroup.MenuGroupCreateResponse;
import kitchenpos.ui.jpa.dto.menugroup.MenuGroupListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestControllerJpa {

    private final MenuGroupServiceJpa menuGroupService;

    public MenuGroupRestControllerJpa(MenuGroupServiceJpa menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/new/api/menu-groups")
    public ResponseEntity<MenuGroupCreateResponse> create(@RequestBody final MenuGroupCreateRequest menuGroupCreateRequest) {
        final MenuGroupCreateResponse menuGroupCreateResponse = menuGroupService.create(menuGroupCreateRequest);
        final URI uri = URI.create("/api/menu-groups/" + menuGroupCreateResponse.getId());
        return ResponseEntity.created(uri).body(menuGroupCreateResponse);
    }

    @GetMapping("/new/api/menu-groups")
    public ResponseEntity<List<MenuGroupListResponse>> list() {
        return ResponseEntity.ok().body(menuGroupService.list());
    }
}
