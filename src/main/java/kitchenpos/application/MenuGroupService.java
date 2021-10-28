package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        return MenuGroupResponse.of(menuGroupDao.save(request.toEntity()));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll().stream()
            .map(MenuGroupResponse::of)
            .collect(Collectors.toList());
    }
}
