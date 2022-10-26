package kitchenpos.ui;

import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.MenuGroupCreationDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupCreationRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
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

    @Deprecated
    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroup> create(@RequestBody final MenuGroup menuGroup) {
        final MenuGroup created = menuGroupService.create(menuGroup);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @PostMapping("/api/v2/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupCreationRequest menuGroup) {
        final MenuGroupDto created = menuGroupService.create(MenuGroupCreationDto.from(menuGroup));
        final MenuGroupResponse menuGroupResponse = MenuGroupResponse.from(created);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri).body(menuGroupResponse);
    }

    @Deprecated
    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroup>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list())
                ;
    }

    @Deprecated
    @GetMapping("/api/v2/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> getMenuGroups() {
        final List<MenuGroupResponse> menuGroupResponses = menuGroupService.getMenuGroups()
                .stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(menuGroupResponses);
    }
}
