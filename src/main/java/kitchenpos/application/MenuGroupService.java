package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.JpaMenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final JpaMenuGroupRepository menuGroupRepository;

    public MenuGroupService(final JpaMenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest request) {
        return menuGroupRepository.save(request.toMenuGroup());
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
