package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuGroupCreateDto;
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
    public MenuGroupDto create(final MenuGroupCreateDto menuGroupCreateDto) {
        final MenuGroup newMenuGroup = new MenuGroup(menuGroupCreateDto.getName());

        final MenuGroup savedMenuGroup = menuGroupDao.save(newMenuGroup);

        return MenuGroupDto.from(savedMenuGroup);
    }

    public List<MenuGroupDto> list() {
        final List<MenuGroup> findMenuGroups = menuGroupDao.findAll();

        return findMenuGroups.stream()
                .map(MenuGroupDto::from)
                .collect(Collectors.toList());
    }
}
