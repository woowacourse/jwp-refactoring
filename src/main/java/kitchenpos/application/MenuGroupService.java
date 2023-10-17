package kitchenpos.application;

import kitchenpos.application.dto.MenuGroupCreateDto;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu.MenuGroup;
import kitchenpos.domain.Menu.MenuGroupName;
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
    public MenuGroup create(MenuGroupCreateDto menuGroupCreateDto) {
        MenuGroup menuGroup = new MenuGroup(
                new MenuGroupName(menuGroupCreateDto.getName()));
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
