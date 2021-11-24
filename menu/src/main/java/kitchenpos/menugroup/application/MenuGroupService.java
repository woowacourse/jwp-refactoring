package kitchenpos.menugroup.application;

import kitchenpos.menugroup.application.dto.MenuGroupRequest;
import kitchenpos.menugroup.application.dto.MenuGroupResponses;
import kitchenpos.menugroup.application.dto.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        final MenuGroup menuGroup = menuGroupWith(request);
        return new MenuGroupResponse(menuGroupRepository.save(menuGroup));
    }

    public MenuGroupResponses list() {
        return new MenuGroupResponses(menuGroupRepository.findAll());
    }

    private MenuGroup menuGroupWith(MenuGroupRequest request) {
        return MenuGroup.builder()
                .name(request.getName())
                .build();
    }
}
