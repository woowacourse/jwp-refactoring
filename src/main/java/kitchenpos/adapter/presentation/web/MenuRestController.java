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
import kitchenpos.application.command.CreateMenuCommand;
import kitchenpos.application.response.MenuResponse;

@RequestMapping(API_MENUS)
@RestController
public class MenuRestController {
    public static final String API_MENUS = "/api/menus";

    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> create(
            @RequestBody @Valid final CreateMenuCommand command) {
        MenuResponse response = menuService.create(command);
        final URI uri = URI.create(API_MENUS + "/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list())
                ;
    }
}
