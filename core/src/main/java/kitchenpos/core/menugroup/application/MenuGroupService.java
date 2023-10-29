package kitchenpos.core.menugroup.application;

import java.util.List;
import kitchenpos.core.menugroup.domain.MenuGroup;
import kitchenpos.core.menugroup.dto.MenuGroupResponse;
import kitchenpos.core.product.domain.Name;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final Name menuGroupName) {
        return MenuGroupResponse.from(menuGroupDao.save(new MenuGroup(menuGroupName)));
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.from(menuGroupDao.findAll());
    }
}
