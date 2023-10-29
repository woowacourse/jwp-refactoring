package kitchenpos.core.application;

import java.util.List;
import kitchenpos.core.domain.menu.MenuGroup;
import kitchenpos.core.domain.menu.repository.MenuGroupRepository;
import kitchenpos.core.dto.request.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroup create(final MenuGroupCreateRequest request) {
        return menuGroupRepository.save(new MenuGroup(request.getName()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
