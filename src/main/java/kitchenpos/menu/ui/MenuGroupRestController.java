package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.dto.MenuGroupDto;
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
    public ResponseEntity<MenuGroupDto> create(@RequestBody final MenuGroupDto menuGroupDto) {
        final MenuGroupDto created = menuGroupService.create(menuGroupDto);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created)
            ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupDto>> list() {
        return ResponseEntity.ok()
            .body(menuGroupService.list())
            ;
    }
}
