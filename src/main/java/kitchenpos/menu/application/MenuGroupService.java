package kitchenpos.menu.application;

import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.ui.dto.MenuGroupCreateRequest;
import kitchenpos.menu.ui.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.save(request.toEntity());
        return MenuGroupResponse.from(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public void validateExistenceById(final long id) {
        if (!menuGroupRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }
}
