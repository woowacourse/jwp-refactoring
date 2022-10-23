package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateMenuGroupDto;
import kitchenpos.application.dto.response.MenuGroupDto;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroup create(final CreateMenuGroupDto menuGroupDto) {
        return menuGroupDao.save(new MenuGroup(menuGroupDto.getName()));
    }

    public List<MenuGroupDto> list() {
        return menuGroupDao.findAll()
                .stream()
                .map(MenuGroupDto::of)
                .collect(Collectors.toList());
    }
}
