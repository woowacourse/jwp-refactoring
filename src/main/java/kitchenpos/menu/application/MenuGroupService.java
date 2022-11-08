package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.request.MenuGroupRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
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
        final MenuGroup savedMenuGroup = menuGroupDao.save(request.toEntity());

        return MenuGroupResponse.of(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll()
                .stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}
