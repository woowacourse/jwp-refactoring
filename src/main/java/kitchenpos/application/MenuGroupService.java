package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dto.MenuGroupDto;
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
    public MenuGroupDto create(final MenuGroupDto menuGroupDto) {
        return menuGroupDao.save(menuGroupDto);
    }

    public List<MenuGroupDto> list() {
        return menuGroupDao.findAll();
    }
}
