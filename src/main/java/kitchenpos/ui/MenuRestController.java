package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.ui.dto.request.CreateMenuRequest;
import kitchenpos.ui.dto.response.CreateMenuResponse;
import kitchenpos.ui.dto.response.ReadMenuResponse;
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
    public ResponseEntity<CreateMenuResponse> create(@RequestBody final CreateMenuRequest request) {
        final Menu created = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + created.getId());
        final CreateMenuResponse response = new CreateMenuResponse(created);

        return ResponseEntity.created(uri)
                             .body(response);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<ReadMenuResponse>> list() {
        final List<ReadMenuResponse> responses = menuService.list()
                                                            .stream()
                                                            .map(ReadMenuResponse::new)
                                                            .collect(Collectors.toList());

        return ResponseEntity.ok()
                             .body(responses);
    }
}
