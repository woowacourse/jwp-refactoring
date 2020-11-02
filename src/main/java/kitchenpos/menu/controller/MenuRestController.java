package kitchenpos.menu.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponses;
import kitchenpos.menu.service.MenuService;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<Void> create(@RequestBody @Valid MenuCreateRequest request) {
        final Long id = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + id);
        return ResponseEntity.created(uri)
            .build();
    }

    @GetMapping("/api/menus")
    public ResponseEntity<MenuResponses> list() {
        return ResponseEntity.ok()
            .body(menuService.list());
    }
}
