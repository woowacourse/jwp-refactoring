package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        final MenuGroup newMenuGroup = new MenuGroup(menuGroupRequest.getName());
        menuGroupRepository.save(newMenuGroup);
        return new MenuGroupResponse(newMenuGroup.getId(), newMenuGroup.getName());
    }

    public List<MenuGroupResponse> findAll() {
        final List<MenuGroup> foundAllMenuGroups = menuGroupRepository.findAll();
        return foundAllMenuGroups.stream()
            .map(foundMenuGroup -> new MenuGroupResponse(foundMenuGroup.getId(), foundMenuGroup.getName()))
            .collect(Collectors.toList())
            ;
    }
}
