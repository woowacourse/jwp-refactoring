package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.dao.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupRequest request) {
        return MenuGroupResponse.from(menuGroupRepository.save(request.toMenuGroup()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
