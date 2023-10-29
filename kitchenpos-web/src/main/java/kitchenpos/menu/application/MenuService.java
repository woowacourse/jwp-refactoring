package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.mapper.MenuMapper;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(final MenuCreateRequest request) {
        final Menu menu = MenuMapper.toMenu(request, convertToMenuProducts(request));
        menuValidator.validate(menu);
        menuRepository.save(menu);

        return MenuMapper.toMenuResponse(menu);
    }

    private List<MenuProduct> convertToMenuProducts(final MenuCreateRequest menuRequest) {
        return menuRequest.getMenuProducts().stream()
                .map(request -> new MenuProduct(request.getProductId(), request.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> readAll() {
        final List<Menu> menus = menuRepository.findAll();

        return MenuMapper.toMenuResponses(menus);
    }
}
