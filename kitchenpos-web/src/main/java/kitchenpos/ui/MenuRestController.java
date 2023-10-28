package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.response.MenuGroupDto;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final CreateMenuRequest menuRequest) {
        final Menu menu = menuService.create(menuRequest);
        final URI uri = URI.create("/api/menus/" + menu.getId());
        return ResponseEntity.created(uri)
                             .body(toResponse(menu));
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        final List<MenuResponse> responses = menuService.list()
                                                        .stream()
                                                        .map(this::toResponse)
                                                        .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok()
                             .body(responses);
    }

    private MenuResponse toResponse(final Menu menu) {
        final MenuGroup menuGroup = menu.getMenuGroup();
        return new MenuResponse(
                menu.getId(),
                new MenuGroupDto(menuGroup.getId(), menuGroup.getName()),
                menu.getPrice(),
                menu.getName()
        );
    }
}
