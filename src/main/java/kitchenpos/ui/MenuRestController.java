package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.service.MenuDto;
import kitchenpos.menu.service.MenuMapper;
import kitchenpos.menu.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {

    private final MenuMapper menuMapper;
    private final MenuService menuService;

    public MenuRestController(MenuMapper menuMapper, final MenuService menuService) {
        this.menuMapper = menuMapper;
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuDto> create(@RequestBody final MenuDto menuDto) {
        final Menu created = menuService.create(menuMapper.toEntity(menuDto));
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(MenuDto.from(created))
            ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuDto>> list() {
        List<MenuDto> menuDtos = menuService.list()
                                         .stream()
                                         .map(MenuDto::from)
                                         .collect(toList());
        return ResponseEntity.ok()
                             .body(menuDtos)
            ;
    }
}
