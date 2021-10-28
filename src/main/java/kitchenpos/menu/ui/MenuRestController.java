package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuCreateRequestDto;
import kitchenpos.menu.dto.MenuCreateResponseDto;
import kitchenpos.menu.dto.MenuReadResponseDto;
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
    public ResponseEntity<MenuCreateResponseDto> create(@RequestBody final MenuCreateRequestDto menuDto) {
        final MenuCreateResponseDto created = menuService.create(menuDto);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created)
            ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuReadResponseDto>> list() {
        return ResponseEntity.ok()
            .body(menuService.list())
            ;
    }
}
