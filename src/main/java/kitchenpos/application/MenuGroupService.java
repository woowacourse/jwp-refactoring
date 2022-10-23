package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuGroupDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    @Transactional
    public MenuGroup create(final CreateMenuGroupDto menuGroupDto) {
        return menuGroupDao.save(new MenuGroup(menuGroupDto.getName()));
    }

    public List<MenuGroupDto> list() {
        return menuGroupDao.findAll()
                .stream()
                .map(MenuGroupDto::of)
                .collect(Collectors.toList());
    }
}
