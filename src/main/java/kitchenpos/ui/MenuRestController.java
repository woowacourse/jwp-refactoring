package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.CreateMenuCommand;
import kitchenpos.application.dto.domain.MenuDto;
import kitchenpos.domain.menu.Menu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuDto> create(@RequestBody final CreateMenuCommand request) {
        final Menu created = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(MenuDto.from(created))
                ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuDto>> list() {
        final List<MenuDto> menuDtos = menuService.list().stream().map(MenuDto::from).collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(menuDtos)
                ;
    }
}
