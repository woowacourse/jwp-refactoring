package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.dto.MenuGroupCreationRequest;
import kitchenpos.application.dto.result.MenuGroupResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/menu-groups")
@RestController
public class MenuGroupRestController {

    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResult> create(@RequestBody final MenuGroupCreationRequest request) {
        final MenuGroupResult created = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResult>> list() {
        final List<MenuGroupResult> response = menuGroupService.list();
        return ResponseEntity.ok()
                .body(response)
                ;
    }
}
