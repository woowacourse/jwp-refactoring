package kitchenpos.menugroup.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(MenuGroupCreateRequest request) {
        MenuGroup menuGroup = new MenuGroup(null, request.getName());
        menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.from(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> findAll() {
        return menuGroupRepository.findAll().stream()
            .map(MenuGroupResponse::from)
            .collect(toList());
    }
}
