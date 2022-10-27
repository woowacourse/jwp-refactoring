package kitchenpos.application;

import java.util.List;
import kitchenpos.application.request.MenuGroupCreateRequest;
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
    public MenuGroup create(final MenuGroupCreateRequest request) {
        return menuGroupDao.save(new MenuGroup(request.getName()));
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
