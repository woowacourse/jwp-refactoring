package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.ui.dto.menugroup.MenuGroupRequest;
import kitchenpos.ui.dto.menugroup.MenuGroupResponse;
import kitchenpos.ui.dto.menugroup.MenuGroupResponses;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        return MenuGroupResponse.from(menuGroupDao.save(request.toEntity()));
    }

    public MenuGroupResponses list() {
        return MenuGroupResponses.from(menuGroupDao.findAll());
    }
}
