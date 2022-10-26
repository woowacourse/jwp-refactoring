package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuGroupCreationDto;
import kitchenpos.application.dto.MenuGroupDto;
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
    @Deprecated
    public MenuGroup create(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    @Transactional
    public MenuGroupDto create(final MenuGroupCreationDto menuGroupCreationDto) {
        final MenuGroup menuGroup = menuGroupDao.save(MenuGroupCreationDto.toEntity(menuGroupCreationDto));
        return MenuGroupDto.from(menuGroup);
    }

    @Deprecated
    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<MenuGroupDto> getMenuGroups() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return menuGroups.stream()
                .map(MenuGroupDto::from)
                .collect(Collectors.toList());
    }
}
