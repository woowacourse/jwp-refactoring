package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = menuGroupDao.save(request.toEntity());
        return MenuGroupResponse.createResponse(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll().stream()
            .map(MenuGroupResponse::createResponse)
            .collect(Collectors.toList());
    }
}
