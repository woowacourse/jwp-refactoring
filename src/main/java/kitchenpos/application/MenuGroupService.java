package kitchenpos.application;

import java.util.List;
import kitchenpos.application.request.MenuGroupCreateRequest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.persistence.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(MenuGroupCreateRequest request) {
        return menuGroupRepository.save(request.toEntity());
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
