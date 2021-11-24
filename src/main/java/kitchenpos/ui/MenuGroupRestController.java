package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.ui.dto.request.menu.MenuGroupRequestDto;
import kitchenpos.ui.dto.response.menu.MenuGroupResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponseDto> create(
        @RequestBody final MenuGroupRequestDto menuGroupRequestDto) {
        final MenuGroupResponseDto responseDto = menuGroupService.create(menuGroupRequestDto);

        final URI uri = URI.create("/api/menu-groups/" + responseDto.getId());
        return ResponseEntity.created(uri)
            .body(responseDto)
            ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponseDto>> list() {
        final List<MenuGroupResponseDto> responseDto = menuGroupService.list();

        return ResponseEntity.ok()
            .body(responseDto)
            ;
    }
}
