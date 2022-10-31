package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.MenuGroupCreationDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.ui.dto.request.MenuGroupCreationRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
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

    @PostMapping("/api/v2/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupCreationRequest menuGroup) {
        final MenuGroupDto created = menuGroupService.create(MenuGroupCreationDto.from(menuGroup));
        final MenuGroupResponse menuGroupResponse = MenuGroupResponse.from(created);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri).body(menuGroupResponse);
    }

    @GetMapping("/api/v2/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> getMenuGroups() {
        final List<MenuGroupResponse> menuGroupResponses = menuGroupService.getMenuGroups()
                .stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(menuGroupResponses);
    }
}
