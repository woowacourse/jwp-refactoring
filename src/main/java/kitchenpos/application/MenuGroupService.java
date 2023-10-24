package kitchenpos.application;

import static java.util.stream.Collectors.toList;

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
        MenuGroup saved = menuGroupDao.save(toEntity(menuGroupDto));
        return MenuGroupDto.from(saved);
    }

    public List<MenuGroupDto> list() {
        return menuGroupDao.findAll()
                           .stream()
                           .map(MenuGroupDto::from)
                           .collect(toList());
    }

    private MenuGroup toEntity(MenuGroupDto menuGroupDto) {
        return new MenuGroup(menuGroupDto.getId(), menuGroupDto.getName());
    }
}
