package kitchenpos.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.dto.MenuGroupCreateRequest;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public Long create(final MenuGroupCreateRequest request) {
        MenuGroup menuGroup = request.toEntity();
        return menuGroupDao.save(menuGroup)
            .getId();
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
