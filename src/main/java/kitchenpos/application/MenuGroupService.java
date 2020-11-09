package kitchenpos.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.ui.dto.MenuGroupResponse;
import kitchenpos.ui.dto.MenuGroupResponses;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroup menuGroup) {
        return MenuGroupResponse.from(menuGroupRepository.save(menuGroup));
    }

    public MenuGroupResponses list() {
        return MenuGroupResponses.from(menuGroupRepository.findAll());
    }
}
