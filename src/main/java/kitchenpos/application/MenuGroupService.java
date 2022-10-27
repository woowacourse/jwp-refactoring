package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        final MenuGroup menuGroup = request.toEntity();
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        return new MenuGroupResponse(savedMenuGroup.getId(), savedMenuGroup.getName());
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll()
                .stream()
                .map(it -> new MenuGroupResponse(it.getId(), it.getName()))
                .collect(Collectors.toList());
    }
}
