package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroup create(final MenuGroup menuGroup) {
        validateName(menuGroup.getName());
        return menuGroupDao.save(menuGroup);
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("유효하지 않은 메뉴 그룹명 : " + name);
        }
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
