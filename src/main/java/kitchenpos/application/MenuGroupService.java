package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupDto create(final MenuGroupDto menuGroupDto) {
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(menuGroupDto.getName()));
        return new MenuGroupDto(savedMenuGroup);
    }

    public List<MenuGroupDto> list() {
        return menuGroupDao.findAll().stream()
                .map(MenuGroupDto::new)
                .collect(Collectors.toList());
    }
}
