package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.MenuRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MenuRestController {

    private final MenuService menuService;

    @PostMapping("/api/menus")
    public ResponseEntity<MenuDto> create(@RequestBody final MenuRequestDto requestBody) {
        final MenuDto created = menuService.create(requestBody.toCreateMenuDto());
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuDto>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }
}
