package kitchenpos.adapter.presentation;

import static kitchenpos.adapter.presentation.MenuGroupRestController.*;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.domain.model.MenuGroup;

@RequestMapping(API_MENU_GROUPS)
@RestController
public class MenuGroupRestController {
    public static final String API_MENU_GROUPS = "/api/menu-groups";

    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest request) {
        final MenuGroupResponse response = menuGroupService.create(request);
        final URI uri = URI.create(API_MENU_GROUPS + "/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @GetMapping
    public ResponseEntity<List<MenuGroup>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list())
                ;
    }
}
