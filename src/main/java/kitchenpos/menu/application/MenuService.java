package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuCreateResponse;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final MenuProductsMapper menuProductsMapper;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator,
            final MenuProductsMapper menuProductsMapper
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.menuProductsMapper = menuProductsMapper;
    }

    public MenuCreateResponse create(final MenuCreateRequest request) {
        final Price price = new Price(request.getPrice());
        final MenuProducts menuProducts = menuProductsMapper.mapFrom(request.getMenuProducts());

        final Menu menu = Menu.of(
                request.getName(),
                price,
                request.getMenuGroupId(),
                menuProducts,
                menuValidator
        );
        final Menu savedMenu = menuRepository.save(menu);

        return MenuCreateResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(toList());
    }
}
