package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupName;
import kitchenpos.ui.dto.MenuGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroup create(final MenuGroupRequest request) {
        final MenuGroupName menuGroupName = new MenuGroupName(request.getName());
        return menuGroupDao.save(new MenuGroup(menuGroupName));
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
