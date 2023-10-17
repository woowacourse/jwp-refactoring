package kitchenpos.application;

import kitchenpos.application.dto.CreateMenuGroupDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupDto create(CreateMenuGroupDto createMenuGroupDto) {
        MenuGroup menuGroup = new MenuGroup(
                new MenuGroupName(createMenuGroupDto.getName()));
        MenuGroup savedManuGroup = menuGroupDao.save(menuGroup);

        return new MenuGroupDto(
                savedManuGroup.getId(),
                savedManuGroup.getName());
    }

    public List<MenuGroupDto> list() {
        return menuGroupDao.findAll().stream()
                .map(menuGroup -> new MenuGroupDto(
                        menuGroup.getId(),
                        menuGroup.getName()
                ))
                .collect(Collectors.toList());
    }
}
