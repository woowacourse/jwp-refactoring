package kitchenpos.menu.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import kitchenpos.menu.service.MenuGroupService;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<Void> create(@RequestBody @Valid MenuGroupCreateRequest request) {
        final Long id = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + id);
        return ResponseEntity.created(uri)
            .build();
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroup>> list() {
        return ResponseEntity.ok()
            .body(menuGroupService.list())
            ;
    }
}
