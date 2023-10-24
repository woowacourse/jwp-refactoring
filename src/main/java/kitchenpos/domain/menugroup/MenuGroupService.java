package kitchenpos.domain.menugroup;

import kitchenpos.application.dto.request.CreateMenuGroupRequest;
import kitchenpos.application.dto.response.CreateMenuGroupResponse;
import kitchenpos.application.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public CreateMenuGroupResponse create(final CreateMenuGroupRequest request) {
        MenuGroup menuGroup = MenuGroupMapper.toMenuGroup(request);
        return CreateMenuGroupResponse.from(menuGroupRepository.save(menuGroup));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
