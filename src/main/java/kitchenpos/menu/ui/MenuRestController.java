package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreateRequestDto;
import kitchenpos.menu.application.dto.MenuResponseDto;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponseDto> create(@RequestBody final MenuCreateRequestDto menuCreateRequest) {
        final MenuResponseDto menuResponse = menuService.create(menuCreateRequest);
        final URI uri = URI.create("/api/menus/" + menuResponse.getId());
        return ResponseEntity.created(uri)
            .body(menuResponse);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponseDto>> list() {
        return ResponseEntity.ok()
            .body(menuService.list());
    }
}
