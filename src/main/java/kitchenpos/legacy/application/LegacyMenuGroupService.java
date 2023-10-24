package kitchenpos.legacy.application;

import java.util.List;
import kitchenpos.legacy.dao.MenuGroupDao;
import kitchenpos.legacy.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LegacyMenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public LegacyMenuGroupService(MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroup create(MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> findAll() {
        return menuGroupDao.findAll();
    }
}
