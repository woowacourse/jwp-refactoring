package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public MenuGroupResponse create(MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = new MenuGroup(menuGroupCreateRequest.getName());
        menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.from(menuGroup);
    }
}
