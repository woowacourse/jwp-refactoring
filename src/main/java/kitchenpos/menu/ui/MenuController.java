package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.request.MenuUpdateRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> create(@Valid @RequestBody final MenuRequest request) {
        final MenuResponse response = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + response.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }

    //TODO: 테스트 코드 작성
    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponse> updateMenu(
        @PathVariable Long menuId,
        @Valid @RequestBody MenuUpdateRequest request
    ) {
        final MenuResponse response = menuService.update(menuId, request);

        return ResponseEntity.ok(response);
    }
}
