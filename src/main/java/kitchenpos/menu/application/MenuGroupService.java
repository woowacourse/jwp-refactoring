package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuGroupCreationDto;
import kitchenpos.menu.application.dto.MenuGroupDto;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupDto create(final MenuGroupCreationDto menuGroupCreationDto) {
        final MenuGroup menuGroup = menuGroupDao.save(MenuGroupCreationDto.toEntity(menuGroupCreationDto));
        return MenuGroupDto.from(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupDto> getMenuGroups() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return menuGroups.stream()
                .map(MenuGroupDto::from)
                .collect(Collectors.toList());
    }
}
