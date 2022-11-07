package kitchenpos.ui.menu;

import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.menu.dto.response.MenuGroupDto;
import kitchenpos.ui.menu.dto.MenuGroupRequestDto;
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

    public MenuGroupRestController(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupDto> create(@RequestBody final MenuGroupRequestDto requestBody) {
        final MenuGroupDto created = menuGroupService.create(requestBody.toCreateMenuGroupDto());
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupDto>> list() {
        return ResponseEntity.ok().body(menuGroupService.list());
    }
}
