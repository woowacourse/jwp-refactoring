package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = menuGroupDao.save(request.toEntity());

        return MenuGroupResponse.from(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll()
            .stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList());
    }
}
