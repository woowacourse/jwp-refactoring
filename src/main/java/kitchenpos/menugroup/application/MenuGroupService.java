package kitchenpos.menugroup.application;

import java.util.List;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest menuGroupRequest) {
        final String name = menuGroupRequest.getName();
        final MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
