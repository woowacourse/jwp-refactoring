package kitchenpos.ui;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.common.dto.request.CreateMenuGroupRequest;
import kitchenpos.common.dto.response.MenuGroupResponse;
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
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final CreateMenuGroupRequest menuGroupRequest) {
        final MenuGroup menuGroup = menuGroupService.create(menuGroupRequest);
        final URI uri = URI.create("/api/menu-groups/" + menuGroup.getId());
        return ResponseEntity.created(uri)
                             .body(toResponse(menuGroup));
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        final List<MenuGroupResponse> responses = menuGroupService.list()
                                                                  .stream()
                                                                  .map(this::toResponse)
                                                                  .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok()
                             .body(responses);
    }

    private MenuGroupResponse toResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(
                menuGroup.getId(),
                menuGroup.getName()
        );
    }
}
