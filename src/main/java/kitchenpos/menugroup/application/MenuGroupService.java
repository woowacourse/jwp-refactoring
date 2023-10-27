package kitchenpos.menugroup.application;

import kitchenpos.menugroup.application.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.application.dto.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public Long create(final MenuGroupCreateRequest menuGroupCreateRequest) {
        final String name = menuGroupCreateRequest.getName();
        final MenuGroup menuGroup = MenuGroup.from(name);
        final MenuGroup saveMenuGroup = menuGroupRepository.save(menuGroup);
        return saveMenuGroup.getId();
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> findAll() {
        return menuGroupRepository.findAll().stream()
                .map(menuGroup -> MenuGroupResponse.of(menuGroup.getId(), menuGroup.getName()))
                .collect(Collectors.toList());
    }
}
