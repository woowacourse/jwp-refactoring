package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupRequest.toEntity();
        MenuGroup save = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.toDto(save);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> list = menuGroupRepository.findAll();

        return list.stream()
                .map(MenuGroupResponse::toDto)
                .collect(Collectors.toList());
    }
}
