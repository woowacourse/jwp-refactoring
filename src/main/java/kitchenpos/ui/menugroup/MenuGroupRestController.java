package kitchenpos.ui.menugroup;

import kitchenpos.application.menugroup.MenuGroupService;
import kitchenpos.application.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.menugroup.dto.MenuGroupResponse;
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
    public ResponseEntity<MenuGroup> create(@RequestBody final MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = menuGroupService.create(menuGroupCreateRequest);
        URI uri = URI.create("/api/menu-groups/" + menuGroup.getId());

        return ResponseEntity.created(uri)
                .body(menuGroup);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        List<MenuGroupResponse> response = menuGroupService.list()
                .stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(response);
    }
}
