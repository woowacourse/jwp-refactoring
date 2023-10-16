package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroup create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = MenuGroup.from(request.getName());
        return menuGroupDao.save(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> readAll() {
        return menuGroupDao.findAll();
    }
}
