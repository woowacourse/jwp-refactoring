package kitchenpos.menuproduct.application;

import kitchenpos.menuproduct.MenuProduct;
import kitchenpos.menuproduct.MenuQuantity;
import kitchenpos.menuproduct.SaveMenuProductsEvent;
import kitchenpos.menuproduct.application.request.MenuProductRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuProductService {
    private final MenuProductRepository menuProductRepository;

    public MenuProductService(final MenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    @EventListener
    public void saveMenuProducts(final SaveMenuProductsEvent event) {
        for (final MenuProductRequest request : event.getRequests()) {
            menuProductRepository.save(
                    new MenuProduct(
                            event.getMenu().getId(),
                            request.getProductId(),
                            new MenuQuantity(request.getQuantity()))
            );
        }
    }
}
