package kitchenpos.application;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.dto.menu.MenuGroupCreateRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        validate(request);
        MenuGroup menuGroup = request.toMenuGroup();
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        return new MenuGroupResponse(savedMenuGroup);
    }

    private void validate(MenuGroupCreateRequest request) {
        String name = request.getName();
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("잘못된 MenuGroup 이름이 입력되었습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupResponse::new)
                .collect(Collectors.toList());
    }
}
