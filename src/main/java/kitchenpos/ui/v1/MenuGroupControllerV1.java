package kitchenpos.ui.v1;

import java.net.URI;
import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menu-groups")
public class MenuGroupControllerV1 {

    private final MenuGroupService menuGroupService;

    public MenuGroupControllerV1(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupCreateRequest request) {
        var response = menuGroupService.create(request);
        URI uri = URI.create("/api/v1/menu-groups/" + response.getId());
        return ResponseEntity.created(uri)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> findAll() {
        return ResponseEntity.ok()
            .body(menuGroupService.findAll());
    }
}
