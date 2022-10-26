package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(request.getName());
        final MenuGroup saved = menuGroupDao.save(menuGroup);
        return new MenuGroupResponse(saved.getId(), saved.getName());
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll()
                .stream()
                .map(it -> new MenuGroupResponse(it.getId(), it.getName()))
                .collect(Collectors.toList());
    }
}
