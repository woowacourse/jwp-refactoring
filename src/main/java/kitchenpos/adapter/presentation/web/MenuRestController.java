package kitchenpos.adapter.presentation.web;

import static kitchenpos.adapter.presentation.web.MenuRestController.*;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.MenuService;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.dto.MenuResponse;

@RequestMapping(API_MENUS)
@RestController
public class MenuRestController {
    public static final String API_MENUS = "/api/menus";

    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final MenuRequest request) {
        Long menuId = menuService.create(request);
        final URI uri = URI.create(API_MENUS + "/" + menuId);
        return ResponseEntity.created(uri)
                .build()
                ;
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list())
                ;
    }
}
