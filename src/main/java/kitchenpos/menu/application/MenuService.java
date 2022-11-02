package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.application.MenuProductDto;
import kitchenpos.menu.dto.request.CreateMenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final CreateMenuRequest request) {
        menuValidator.validateCreateMenu(request);

        final List<MenuProductDto> menuProducts = request.getMenuProducts().stream()
            .map(it -> new MenuProductDto(it.getProductId(), it.getQuantity()))
            .collect(Collectors.toList());

        final Menu menu = menuRepository.save(new Menu(
            request.getName(),
            request.getPrice(),
            request.getMenuGroupId(),
            menuProducts
        ));

        return new MenuResponse(menu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::new)
            .collect(Collectors.toList());
    }

}
