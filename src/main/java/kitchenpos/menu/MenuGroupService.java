package kitchenpos.menu;

import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final JpaMenuGroupRepository jpaMenuGroupRepository;

    public MenuGroupService(final JpaMenuGroupRepository jpaMenuGroupRepository) {
        this.jpaMenuGroupRepository = jpaMenuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = new MenuGroup(request.getName());
        MenuGroup savedMenuGroup = jpaMenuGroupRepository.save(menuGroup);
        return new MenuGroupResponse(savedMenuGroup);
    }

    public List<MenuGroup> list() {
        return jpaMenuGroupRepository.findAll();
    }

}
