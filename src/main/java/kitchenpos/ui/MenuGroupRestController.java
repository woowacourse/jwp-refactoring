package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupMapper;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import kitchenpos.ui.dto.response.MenuGroupCreateResponse;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {

    private final MenuGroupMapper menuGroupMapper;
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupMapper menuGroupMapper, final MenuGroupService menuGroupService) {
        this.menuGroupMapper = menuGroupMapper;
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupCreateResponse> create(
            @RequestBody final MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = menuGroupMapper.menuGroupCreateRequestToMenuGroup(menuGroupCreateRequest);
        MenuGroupCreateResponse created = menuGroupMapper.menuGroupToMenuGroupCreateResponse(
                menuGroupService.create(menuGroup)
        );
        URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        List<MenuGroupResponse> menuGroupResponses = menuGroupMapper.menuGroupsToMenuGroupResponses(
                menuGroupService.list()
        );
        return ResponseEntity.ok().body(menuGroupResponses);
    }
}
