package kitchenpos.application;

import static java.util.stream.Collectors.groupingBy;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuVerifier;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuVerifier menuVerifier;

    public MenuService(
        MenuRepository menuRepository,
        MenuProductRepository menuProductRepository,
        MenuGroupRepository menuGroupRepository,
        MenuVerifier menuVerifier
    ) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuVerifier = menuVerifier;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        if (Objects.isNull(menuCreateRequest.getMenuGroupId()) ||
            !menuGroupRepository.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹을 찾지 못했습니다.");
        }

        List<MenuProduct> menuProducts = menuCreateRequest.getMenuProducts()
            .stream()
            .map(MenuProductCreateRequest::toEntity)
            .collect(Collectors.toList());

        menuVerifier.verifyPrice(menuCreateRequest.getPrice(), menuProducts);

        Menu savedMenu = menuRepository.save(menuCreateRequest.toEntity());

        List<MenuProduct> savedMenuProducts = menuProductRepository.saveAll(
            menuProducts.stream()
                .peek(it -> it.changeMenuId(savedMenu.getId()))
                .collect(Collectors.toList())
        );

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        Map<Long, Menu> allMenus = menuRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Menu::getId, it -> it));
        Map<Menu, List<MenuProduct>> menuProductsGroup = menuProductRepository
            .findAllByMenuIdIn(allMenus.keySet())
            .stream()
            .collect(groupingBy(it -> allMenus.get(it.getMenuId())));

        return allMenus.values()
            .stream()
            .map(it -> MenuResponse.of(
                it,
                menuProductsGroup.getOrDefault(it, Collections.emptyList())
            ))
            .collect(Collectors.toList());
    }
}
