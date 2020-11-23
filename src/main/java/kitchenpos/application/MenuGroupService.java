package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest menuGroupCreateRequest) {
        return MenuGroupResponse.from(menuGroupDao.save(menuGroupCreateRequest.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.listOf(menuGroupDao.findAll());
    }
}
