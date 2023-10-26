package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.dto.menugroup.request.MenuGroupCreateRequest;
import kitchenpos.dto.menugroup.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public Long create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = request.toEntity();
        return menuGroupRepository.save(menuGroup).id();
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
