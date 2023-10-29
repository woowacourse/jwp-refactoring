package kitchenpos.application.menugroup;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupMapper;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuGroupMapper menuGroupMapper;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository, final MenuGroupMapper menuGroupMapper) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuGroupMapper = menuGroupMapper;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = menuGroupMapper.toMenuGroup(request);
        return MenuGroupResponse.toResponse(menuGroupRepository.save(menuGroup));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupResponse::toResponse)
                .collect(Collectors.toList());
    }
}
