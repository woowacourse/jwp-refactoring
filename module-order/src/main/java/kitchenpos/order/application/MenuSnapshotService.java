package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.MenuProductSnapshot;
import kitchenpos.order.domain.MenuSnapshot;
import kitchenpos.order.domain.repository.MenuSnapshotRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuSnapshotService {

    private final MenuRepository menuRepository;
    private final MenuSnapshotRepository menuSnapshotRepository;

    public MenuSnapshotService(final MenuRepository menuRepository, final MenuSnapshotRepository menuSnapshotRepository) {
        this.menuRepository = menuRepository;
        this.menuSnapshotRepository = menuSnapshotRepository;
    }

    public MenuSnapshot getMenuSnapshotFor(final Long menuId) {
        final Menu menu = menuRepository.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);

        return menuSnapshotRepository.findByMenuIdAndMenuUpdatedAt(menuId, menu.getUpdatedAt())
                .orElseGet(() -> menuSnapshotRepository.save(createSnapShot(menu)));
    }

    private MenuSnapshot createSnapShot(final Menu menu) {
        final List<MenuProductSnapshot> menuProductSnapShots = menu.getMenuProducts().stream()
                .map(menuProduct -> new MenuProductSnapshot(
                        menuProduct.getProductName(),
                        menuProduct.getProductPrice(),
                        menuProduct.getQuantity()
                ))
                .collect(Collectors.toList());
        return new MenuSnapshot(
                menu.getId(),
                menu.getName(),
                menu.getMenuGroupName(),
                menu.getPrice(),
                menu.getUpdatedAt(),
                menuProductSnapShots
        );
    }
}
