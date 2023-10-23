package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.application.MenuService;
import kitchenpos.dto.request.MenuRequset;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final MenuRequset requset) {
        final Long menuId = menuService.create(requset);
        return ResponseEntity.created(URI.create("/api/menus/" + menuId)).build();
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }
}
