package kitchenpos.menu.presentation;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.presentation.dto.request.MenuRequest;
import kitchenpos.menu.presentation.dto.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {
    private final MenuService menuService;

    public MenuController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody MenuRequest menuRequest) {

        final Menu created = menuService.create(menuRequest);
        final MenuResponse menuResponse = MenuResponse.from(created);
        final URI uri = URI.create("/api/menus/" + created.getId());

        return ResponseEntity.created(uri)
                .body(menuResponse);
    }

    @GetMapping("/api/menus")
    @ResponseStatus(OK)
    public List<MenuResponse> list() {

        final List<Menu> menus = menuService.list();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(toList());
    }
}
