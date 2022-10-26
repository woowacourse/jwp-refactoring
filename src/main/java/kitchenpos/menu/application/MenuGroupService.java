package kitchenpos.menu.application;

import kitchenpos.menu.repository.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.ui.dto.MenuGroupCreateRequest;
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
    public MenuGroup create(final MenuGroupCreateRequest request) {
        return menuGroupDao.save(request.toMenuGroup());
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
