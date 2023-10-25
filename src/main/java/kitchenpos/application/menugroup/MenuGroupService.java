package kitchenpos.application.menugroup;

import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupMapper;
import kitchenpos.domain.menugroup.MenuGroupRepository;
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
    private final MenuGroupMapper menuGroupMapper;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository, MenuGroupMapper menuGroupMapper) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuGroupMapper = menuGroupMapper;
    }

    @Transactional
    public CreateMenuGroupResponse create(final CreateMenuGroupRequest request) {
        MenuGroup menuGroup = menuGroupMapper.toMenuGroup(request);
        return CreateMenuGroupResponse.from(menuGroupRepository.save(menuGroup));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
