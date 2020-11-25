package kitchenpos.application;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.dto.menu.MenuGroupCreateRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        MenuGroup menuGroup = request.toMenuGroup();
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        return new MenuGroupResponse(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupResponse::new)
                .collect(Collectors.toList());
    }
}
