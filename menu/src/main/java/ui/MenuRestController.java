package ui;

import dto.request.MenuCreateRequest;
import dto.response.MenuResponse;
import application.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody MenuCreateRequest request) {
        MenuResponse response = menuService.create(request);
        URI uri = URI.create("/api/menus/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> readAll() {
        return ResponseEntity.ok().body(menuService.readAll());
    }
}
