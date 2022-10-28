package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.request.MenuGroupRequest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest menuGroup) {
        return menuGroupDao.save(menuGroup.toEntity());
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
