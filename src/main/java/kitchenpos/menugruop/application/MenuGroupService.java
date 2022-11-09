package kitchenpos.menugruop.application;

import java.util.List;
import kitchenpos.menugruop.application.dto.MenuGroupCreateRequest;
import kitchenpos.menugruop.application.dto.MenuGroupResponse;
import kitchenpos.menugruop.domain.MenuGroup;
import kitchenpos.menugruop.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.save(request.toMenuGroup());
        return MenuGroupResponse.from(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupResponse.from(menuGroups);
    }
}
