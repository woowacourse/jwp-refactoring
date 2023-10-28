package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.menugroup.MenuGroupService;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupDto;
import kitchenpos.menugroup.MenuGroupMapper;
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
    public ResponseEntity<MenuGroupDto> create(@RequestBody final MenuGroupDto menuGroupDto) {
        final MenuGroup created = menuGroupService.create(MenuGroupMapper.toEntity(menuGroupDto));
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(MenuGroupMapper.toDto(created))
            ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupDto>> list() {
        List<MenuGroupDto> menuGroups = menuGroupService.list()
                                                        .stream()
                                                        .map(MenuGroupMapper::toDto)
                                                        .collect(toList());
        return ResponseEntity.ok()
                             .body(menuGroups)
            ;
    }
}
