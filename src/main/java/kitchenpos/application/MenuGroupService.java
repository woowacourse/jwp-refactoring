package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.ui.request.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = new MenuGroup(menuGroupCreateRequest.getName());

        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
