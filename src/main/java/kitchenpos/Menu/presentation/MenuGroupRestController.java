package kitchenpos.Menu.presentation;

import kitchenpos.Menu.application.MenuGroupService;
import kitchenpos.Menu.domain.MenuGroup;
import kitchenpos.Menu.domain.dto.request.MenuGroupCreateRequest;
import kitchenpos.Menu.domain.dto.response.MenuGroupResponse;
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
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupCreateRequest request) {
        final MenuGroup created = menuGroupService.create(new MenuGroup(request.getName()));
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(MenuGroupResponse.toDTO(created))
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        List<MenuGroupResponse> response = menuGroupService.list().stream()
                .map(MenuGroupResponse::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(response)
                ;
    }
}
