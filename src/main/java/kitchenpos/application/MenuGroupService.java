package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(MenuGroupRequest request) {
        MenuGroup menuGroup = new MenuGroup(request.getName());
        return MenuGroupResponse.from(menuGroupRepository.save(menuGroup));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
