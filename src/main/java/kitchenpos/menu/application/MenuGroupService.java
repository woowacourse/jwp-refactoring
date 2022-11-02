package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroup create(final MenuGroupCreateRequest request) {
        return menuGroupDao.save(new MenuGroup(request.getName()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
