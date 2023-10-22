package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.application.dto.menugroup.MenuGroupCreateResponse;
import kitchenpos.application.dto.menugroup.MenuGroupResponse;
import kitchenpos.domain.MenuGroup;
import kitchenpos.persistence.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupCreateResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        return MenuGroupCreateResponse.of(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(toList());
    }
}
