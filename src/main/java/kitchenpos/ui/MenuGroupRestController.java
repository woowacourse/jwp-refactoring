package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.menugroup.service.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.service.MenuGroupDto;
import kitchenpos.menugroup.service.MenuGroupMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {

    private final MenuGroupMapper menuGroupMapper;
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupMapper menuGroupMapper, final MenuGroupService menuGroupService) {
        this.menuGroupMapper = menuGroupMapper;
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupDto> create(@RequestBody final MenuGroupDto menuGroupDto) {
        final MenuGroup created = menuGroupService.create(menuGroupMapper.toEntity(menuGroupDto));
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(MenuGroupDto.from(created))
            ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupDto>> list() {
        List<MenuGroupDto> menuGroups = menuGroupService.list()
                                                        .stream()
                                                        .map(MenuGroupDto::from)
                                                        .collect(toList());
        return ResponseEntity.ok()
                             .body(menuGroups)
            ;
    }
}
