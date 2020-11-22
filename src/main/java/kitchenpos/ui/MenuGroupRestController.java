package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
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

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> respondCreatedMenuGroupResponse(
            @RequestBody final MenuGroupCreateRequest menuGroupCreateRequest
    ) {
        final MenuGroupResponse menuGroupResponse = menuGroupService.createMenuGroup(menuGroupCreateRequest);
        final URI uri = URI.create("/api/menu-groups/" + menuGroupResponse.getId());

        return ResponseEntity.created(uri)
                .body(menuGroupResponse);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> respondAllMenuGroupResponses() {
        return ResponseEntity.ok()
                .body(menuGroupService.listAllMenuGroups());
    }
}
