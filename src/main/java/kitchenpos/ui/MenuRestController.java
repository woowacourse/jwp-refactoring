package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.MenuDtoMapper;
import kitchenpos.ui.dto.MenuMapper;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.response.MenuCreateResponse;
import kitchenpos.ui.dto.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {

    private final MenuMapper menuMapper;
    private final MenuDtoMapper menuDtoMapper;
    private final MenuService menuService;

    public MenuRestController(final MenuMapper menuMapper, final MenuDtoMapper menuDtoMapper,
                              final MenuService menuService) {
        this.menuMapper = menuMapper;
        this.menuDtoMapper = menuDtoMapper;
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuCreateResponse> create(@RequestBody final MenuCreateRequest menuCreateRequest) {
        Menu menu = menuMapper.menuCreateRequestToMenu(menuCreateRequest);
        MenuCreateResponse created = menuDtoMapper.menuToMenuCreateResponse(menuService.create(menu));
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        List<MenuResponse> menuResponses = menuDtoMapper.menusToMenuResponses(
                menuService.list()
        );
        return ResponseEntity.ok().body(menuResponses);
    }
}
