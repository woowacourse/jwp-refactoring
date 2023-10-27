package kitchenpos.menugroup.application;

import java.util.List;
import kitchenpos.menugroup.application.dto.MenuGroupCreateRequest;
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
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = createMenuGroupByRequest(request);
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        
        return MenuGroupResponse.from(savedMenuGroup);
    }

    private MenuGroup createMenuGroupByRequest(final MenuGroupCreateRequest request) {
        return new MenuGroup(request.getName());
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.listOf(menuGroupRepository.findAll());
    }
}
