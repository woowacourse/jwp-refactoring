package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuGroupCreateDto;
import kitchenpos.application.dto.MenuGroupResponseDto;
import kitchenpos.application.dto.MenuResponseDto;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponseDto create(final MenuGroupCreateDto dto) {
        final MenuGroup menuGroup = new MenuGroup(dto.getName());
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        return new MenuGroupResponseDto(
                savedMenuGroup.getId(),
                savedMenuGroup.getName()
        );
    }

    public List<MenuGroupResponseDto> list() {
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return menuGroups.stream()
                .map(menuGroup -> new MenuGroupResponseDto(
                        menuGroup.getId(),
                        menuGroup.getName())
                ).collect(Collectors.toList());
    }
}
