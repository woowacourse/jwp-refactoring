package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.MenuGroupCreationRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.domain.MenuGroup;
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

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupCreationRequest request) {
        final MenuGroup created = menuGroupService.create(request);
        final MenuGroupResponse response = MenuGroupResponse.from(created);
        final URI uri = URI.create("/api/menu-groups/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        final List<MenuGroupResponse> response = menuGroupService.list()
                .stream().map(MenuGroupResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(response)
                ;
    }
}
