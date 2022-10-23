package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
    public MenuGroup create(final MenuGroup menuGroup) {
        if (menuGroup.getName() == null) {
            throw new IllegalArgumentException("이름은 null일 수 없습니다.");
        }
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> findAll() {
        return menuGroupDao.findAll();
    }
}
