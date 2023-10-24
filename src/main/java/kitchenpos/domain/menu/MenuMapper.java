package kitchenpos.domain.menu;

import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.domain.menugroup.MenuGroupRepository;
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
        validateMenuGroupId(createMenuRequest.getMenuGroupId());
        return Menu.builder()
                .name(createMenuRequest.getName())
                .price(createMenuRequest.getPrice())
                .menuGroupId(createMenuRequest.getMenuGroupId())
                .menuProducts(createMenuRequest.getMenuProducts()
                        .stream()
                        .map(menuProductMapper::toMenuProduct)
                        .collect(Collectors.toList()))
                .build();
    }

    private void validateMenuGroupId(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }
}
