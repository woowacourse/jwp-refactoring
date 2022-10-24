package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
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
    public MenuGroup create(MenuGroupCreateRequest menuGroupCreateRequest) {
        if (menuGroupCreateRequest.getName() == null) {
            throw new IllegalArgumentException();
        }
        return menuGroupDao.save(new MenuGroup(menuGroupCreateRequest.getName()));
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
