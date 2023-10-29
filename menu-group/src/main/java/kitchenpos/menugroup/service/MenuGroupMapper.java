package kitchenpos.menugroup.service;

import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuGroupMapper {

    public MenuGroup toEntity(MenuGroupDto menuGroupDto) {
        return new MenuGroup.Builder()
            .id(menuGroupDto.getId())
            .name(menuGroupDto.getName())
            .build();
    }
}
