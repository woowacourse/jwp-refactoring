package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
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
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        MenuGroup menuGroup = new MenuGroup(null, request.getMenuGroupName());
        return MenuGroupResponse.of(menuGroupRepository.save(menuGroup));
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> allMenuGroup = menuGroupRepository.findAll();
        return MenuGroupResponse.of(allMenuGroup);
    }
}
