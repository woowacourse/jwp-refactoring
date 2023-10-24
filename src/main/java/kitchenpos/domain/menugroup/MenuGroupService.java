package kitchenpos.domain.menugroup;

import kitchenpos.dto.request.CreateMenuGroupRequest;
import kitchenpos.dto.response.CreateMenuGroupResponse;
import kitchenpos.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
