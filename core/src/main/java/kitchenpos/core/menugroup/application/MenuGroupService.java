package kitchenpos.core.menugroup.application;

import kitchenpos.core.menugroup.domain.MenuGroup;
import kitchenpos.core.menugroup.repository.MenuGroupRepository;
import kitchenpos.core.menugroup.presentation.dto.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroup create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        return menuGroupRepository.save(menuGroup);
    }

    @Transactional(readOnly = true)
    public MenuGroup findById(final Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴그룹이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> findAll() {
        return menuGroupRepository.findAll();
    }
}
