package kitchenpos.menu.application;

import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.presentation.dto.request.MenuGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupDao;

    public MenuGroupService(final MenuGroupRepository menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest menuGroupRequest) {

        final MenuGroup menuGroup = menuGroupRequest.toDomain();

        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> list() {

        return menuGroupDao.findAll();
    }
}
