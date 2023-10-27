package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuValidator menuValidator;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    public MenuService(
            MenuValidator menuValidator,
            MenuGroupRepository menuGroupRepository,
            MenuRepository menuRepository
    ) {
        this.menuValidator = menuValidator;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest request) {
        Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                menuGroupRepository.getById(request.getMenuGroupId()),
                request.toMenuProducts()
        );
        menu.register(menuValidator);
        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
