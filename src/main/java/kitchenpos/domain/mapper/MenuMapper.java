package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MenuMapper {

    private final MenuProductMapper menuProductMapper;
    private final MenuGroupRepository menuGroupRepository;

    private MenuMapper(MenuProductMapper menuProductMapper, MenuGroupRepository menuGroupRepository) {
        this.menuProductMapper = menuProductMapper;
        this.menuGroupRepository = menuGroupRepository;
    }

    public Menu toMenu(final CreateMenuRequest createMenuRequest) {
        return Menu.builder()
                .name(createMenuRequest.getName())
                .price(createMenuRequest.getPrice())
                .menuGroup(getMenuGroupById(createMenuRequest.getMenuGroupId()))
                .menuProducts(createMenuRequest.getMenuProducts()
                        .stream()
                        .map(menuProductMapper::toMenuProduct)
                        .collect(Collectors.toList()))
                .build();
    }

    private MenuGroup getMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));
    }
}
