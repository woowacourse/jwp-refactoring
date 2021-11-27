package kitchenpos.ui.menu;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.dto.request.MenuRequestDto;
import kitchenpos.dto.response.MenuResponseDto;
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
    public ResponseEntity<MenuResponseDto> create(
        @RequestBody final MenuRequestDto menuRequestDto) {
        final MenuResponseDto responseDto = menuService.create(menuRequestDto);

        final URI uri = URI.create("/api/menus/" + responseDto.getId());
        return ResponseEntity.created(uri)
            .body(responseDto)
            ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponseDto>> list() {
        List<MenuResponseDto> responseDto = menuService.list();

        return ResponseEntity.ok()
            .body(responseDto)
            ;
    }
}
