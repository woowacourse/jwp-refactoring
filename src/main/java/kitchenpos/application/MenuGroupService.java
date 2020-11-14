package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupCreateRequest;
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
        return menuGroupDao.save(request.toEntity());
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
