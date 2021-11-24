package kitchenpos.menugroup.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menugroup.service.dto.MenuGroupRequest;
import kitchenpos.menugroup.service.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupRequest request) {
        return MenuGroupResponse.of(menuGroupRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
            .map(MenuGroupResponse::of)
            .collect(Collectors.toList());
    }
}
