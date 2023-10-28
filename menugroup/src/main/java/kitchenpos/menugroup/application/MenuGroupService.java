package kitchenpos.menugroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.application.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.application.response.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = MenuGroup.builder()
                .name(menuGroupCreateRequest.getName())
                .build();
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.from(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
