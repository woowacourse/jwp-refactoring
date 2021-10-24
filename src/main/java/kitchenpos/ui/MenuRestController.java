package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.request.MenuRequestDto;
import kitchenpos.application.dto.response.MenuResponseDto;
import kitchenpos.ui.dto.request.MenuRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody MenuRequest menuRequest) {
        MenuRequestDto menuRequestDto = menuRequest.toDto();
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);
        MenuResponse menuResponse = MenuResponse.from(menuResponseDto);
        URI uri = URI.create("/api/menus/" + menuResponse.getId());
        return ResponseEntity.created(uri)
            .body(menuResponse);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        List<MenuResponse> menuResponses = menuService.list()
            .stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok()
            .body(menuResponses);
    }
}
