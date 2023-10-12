package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class ServiceIntegrationTest {

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    @Autowired
    protected ProductDao productDao;


    private MenuProduct 메뉴_상품_저장(MenuProduct menuProduct) {
        Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(NoSuchElementException::new);
        Product savedProduct = productDao.save(product);

        return new MenuProduct(savedProduct.getId(), menuProduct.getQuantity());
    }

    private MenuGroup 메뉴_그룹_저장(MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    public Menu 메뉴_생성(
            BigDecimal price,
            MenuGroup menuGroup,
            MenuProduct menuProduct
    ) {
        MenuProduct 메뉴_상품 = 메뉴_상품_저장(menuProduct);
        MenuGroup 저장된_메뉴_그룹 = 메뉴_그룹_저장(menuGroup);

        return new Menu(
                "메뉴",
                price,
                저장된_메뉴_그룹.getId(),
                List.of(메뉴_상품)
        );
    }

    public Menu 존재하지_않는_상품을_MenuProduct_로_가진_메뉴_생성(
            BigDecimal price,
            MenuGroup menuGroup
    ) {
        MenuProduct 메뉴_상품 = new MenuProduct(Long.MAX_VALUE, 0);
        MenuGroup 저장된_메뉴_그룹 = 메뉴_그룹_저장(menuGroup);

        return new Menu(
                "메뉴",
                price,
                저장된_메뉴_그룹.getId(),
                List.of(메뉴_상품)
        );
    }

    public Menu 존재하지_않는_MenuGroup_을_가진_메뉴_생성(
            BigDecimal price,
            MenuProduct menuProduct
    ) {
        MenuProduct 메뉴_상품 = 메뉴_상품_저장(menuProduct);

        return new Menu(
                "메뉴",
                price,
                Long.MAX_VALUE,
                List.of(메뉴_상품)
        );
    }

}
