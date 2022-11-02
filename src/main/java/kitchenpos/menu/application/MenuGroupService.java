package kitchenpos.menu.application;

import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.presentation.dto.request.MenuGroupRequest;
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
    public MenuGroup create(MenuGroupRequest menuGroupRequest) {

        final MenuGroup menuGroup = menuGroupRequest.toDomain();

        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup> list() {

        return menuGroupRepository.findAll();
    }
}
