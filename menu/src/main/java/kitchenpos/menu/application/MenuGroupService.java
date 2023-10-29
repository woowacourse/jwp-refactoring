package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import kitchenpos.menu.application.mapper.MenuGroupMapper;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.persistence.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        return MenuGroupMapper.mapToMenuGroupResponse(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        final List<MenuGroup> savedMenuGroups = menuGroupRepository.findAll();
        return savedMenuGroups.stream()
                .map(MenuGroupMapper::mapToMenuGroupResponse)
                .collect(Collectors.toList());
    }
}
