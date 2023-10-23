package kitchenpos.application;

import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.application.dto.response.CreateMenuResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.mapper.MenuMapper;
import kitchenpos.domain.mapper.MenuProductMapper;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuMapper menuMapper;
    private final MenuProductMapper menuProductMapper;
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuMapper menuMapper,
            final MenuProductMapper menuProductMapper,
            final MenuRepository menuRepository
    ) {
        this.menuMapper = menuMapper;
        this.menuProductMapper = menuProductMapper;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public CreateMenuResponse create(final CreateMenuRequest request) {
        final Menu menu = menuMapper.toMenu(request);
        Menu updated = menu.updateMenuProducts(request.getMenuProducts().stream()
                .map(menuProductMapper::toMenuProduct)
                .collect(Collectors.toList())
        );

        return CreateMenuResponse.from(menuRepository.save(updated));
    }

    public List<MenuResponse> list() {

        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
