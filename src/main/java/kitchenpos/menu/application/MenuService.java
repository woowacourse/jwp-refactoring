package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductMapper menuProductMapper;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            MenuProductMapper menuProductMapper
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductMapper = menuProductMapper;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        MenuGroup menuGroup = getMenuGroup(menuCreateRequest);
        Menu menu = new Menu(
                menuCreateRequest.getName(),
                menuCreateRequest.getPrice(),
                menuGroup,
                menuCreateRequest.toMenuProductQuantities(),
                menuProductMapper
        );
        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }

    private MenuGroup getMenuGroup(MenuCreateRequest menuCreateRequest) {
        return menuGroupRepository.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 메뉴 그룹입니다."));
    }
}
