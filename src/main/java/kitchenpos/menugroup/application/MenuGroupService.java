package kitchenpos.menugroup.application;

import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupName;
import kitchenpos.menugroup.application.request.MenuGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroup create(final MenuGroupRequest request) {
        final MenuGroupName menuGroupName = new MenuGroupName(request.getName());
        return menuGroupRepository.save(new MenuGroup(menuGroupName));
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
