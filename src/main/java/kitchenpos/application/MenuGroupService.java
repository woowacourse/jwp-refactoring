package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.menu.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroup create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = createMenuGroupByRequest(request);
        return menuGroupDao.save(menuGroup);
    }

    private MenuGroup createMenuGroupByRequest(final MenuGroupCreateRequest request) {
        return new MenuGroup(request.getName());
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
