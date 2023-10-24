package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.MenuGroupCreateDto;
import kitchenpos.dto.MenuGroupDto;
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
    public ResponseEntity<MenuGroupDto> create(@RequestBody final MenuGroupCreateDto request) {
        final MenuGroupDto response = menuGroupService.create(request);

        final URI uri = URI.create("/api/menu-groups/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupDto>> list() {
        final List<MenuGroupDto> response = menuGroupService.list();

        return ResponseEntity.ok().body(response);
    }
}
