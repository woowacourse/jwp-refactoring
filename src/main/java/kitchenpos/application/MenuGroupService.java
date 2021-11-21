package kitchenpos.application;

import kitchenpos.application.dtos.MenuGroupRequest;
import kitchenpos.application.dtos.MenuGroupResponse;
import kitchenpos.application.dtos.MenuGroupResponses;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
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
