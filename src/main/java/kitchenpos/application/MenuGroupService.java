package kitchenpos.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.ui.dto.menugroup.MenuGroupRequest;
import kitchenpos.ui.dto.menugroup.MenuGroupResponse;
import kitchenpos.ui.dto.menugroup.MenuGroupResponses;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroup) {
        return MenuGroupResponse.from(menuGroupRepository.save(menuGroup.toEntity()));
    }

    public MenuGroupResponses list() {
        return MenuGroupResponses.from(menuGroupRepository.findAll());
    }
}
