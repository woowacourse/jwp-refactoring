package kitchenpos.menugroup.application;

import static java.util.stream.Collectors.*;

import kitchenpos.menugroup.domain.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.dto.MenuGroupSaveRequest;
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
    public MenuGroupResponse create(final MenuGroupSaveRequest request) {
        MenuGroup menuGroup = menuGroupDao.save(request.toEntity());
        return new MenuGroupResponse(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll()
                .stream()
                .map(MenuGroupResponse::new)
                .collect(toList());
    }
}
