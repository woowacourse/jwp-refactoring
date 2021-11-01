package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroupRequest.toEntity());
        return MenuGroupResponse.from(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.listFrom(menuGroupRepository.findAll());
    }
}
