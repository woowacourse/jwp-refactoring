package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.ui.request.MenuRequest;
import kitchenpos.ui.response.MenuProductResponse;
import kitchenpos.ui.response.MenuResponse;
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
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest request) {
        final Menu menu = menuService.create(request);
        return findById(menu.getId());
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        List<MenuResponse> body = menuService.list().stream()
                .map(this::createResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(body);
    }

    private ResponseEntity<MenuResponse> findById(final Long id) {
        final Menu menu = menuService.findById(id);
        final MenuResponse body = createResponse(menu);
        final URI uri = URI.create("/api/menus/" + body.getId());
        return ResponseEntity.created(uri).body(body);
    }

    private MenuResponse createResponse(final Menu menu) {
        final List<MenuProductResponse> products = menu.getMenuProducts().stream()
                .map(p -> new MenuProductResponse(p.getSeq(), p.getProductId(), p.getQuantity()))
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), products);
    }
}
