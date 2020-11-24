package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MenuRestController {
    private final MenuService menuService;

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(
        @Valid @RequestBody final MenuCreateRequest menuCreateRequest
    ) {
        final Menu created = menuService.create(menuCreateRequest.toRequestEntity());
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
            .body(MenuResponse.from(created));
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
            .body(menuService.list().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList()));
    }
}
