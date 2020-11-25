package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequestDto;
import kitchenpos.dto.MenuGroupResponseDto;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponseDto create(final MenuGroupCreateRequestDto menuGroupCreateRequest) {
        MenuGroup menuGroup = menuGroupCreateRequest.toEntity();
        MenuGroup saved = menuGroupDao.save(menuGroup);
        return MenuGroupResponseDto.from(saved);
    }

    public List<MenuGroupResponseDto> list() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return MenuGroupResponseDto.listOf(menuGroups);
    }
}
