package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuProductDaoTest extends KitchenPosDaoTest {

    private static final int TEST_QUANTITY = 3;

    @Autowired
    private MenuProductDao menuProductDao;

    @DisplayName("MenuProduct 저장 - 성공")
    @Test
    void save_Success() {
        Long menuId = getCreatedMenuId();
        Long productId = getCreatedProductId();

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(TEST_QUANTITY);

        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        assertThat(savedMenuProduct.getSeq()).isNotNull();
        assertThat(savedMenuProduct.getMenuId()).isEqualTo(menuId);
        assertThat(savedMenuProduct.getProductId()).isEqualTo(productId);
        assertThat(savedMenuProduct.getQuantity()).isEqualTo(TEST_QUANTITY);
    }

    @DisplayName("MenuProduct ID로 MenuProduct 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(getCreatedMenuId());
        menuProduct.setProductId(getCreatedProductId());
        menuProduct.setQuantity(TEST_QUANTITY);
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        MenuProduct foundMenuProduct = menuProductDao.findById(savedMenuProduct.getSeq())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 MenuProduct가 없습니다."));

        assertThat(foundMenuProduct.getSeq()).isEqualTo(savedMenuProduct.getSeq());
        assertThat(foundMenuProduct.getMenuId()).isEqualTo(savedMenuProduct.getMenuId());
        assertThat(foundMenuProduct.getProductId()).isEqualTo(savedMenuProduct.getProductId());
        assertThat(foundMenuProduct.getQuantity()).isEqualTo(savedMenuProduct.getQuantity());
    }

    @DisplayName("MenuProduct ID로 MenuProduct 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_ExistsId_ReturnEmpty() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(getCreatedMenuId());
        menuProduct.setProductId(getCreatedProductId());
        menuProduct.setQuantity(TEST_QUANTITY);
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        Optional<MenuProduct> foundMenuProduct = menuProductDao
            .findById(savedMenuProduct.getSeq() + 1);

        assertThat(foundMenuProduct.isPresent()).isFalse();
    }

    @DisplayName("전체 MenuProduct 조회 - 성공")
    @Test
    void findAll_Success() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(getCreatedMenuId());
        menuProduct.setProductId(getCreatedProductId());
        menuProduct.setQuantity(TEST_QUANTITY);
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        List<MenuProduct> menuProducts = menuProductDao.findAll();

        assertThat(menuProducts).isNotNull();
        assertThat(menuProducts).isNotEmpty();

        List<Long> menuProductIds = menuProducts.stream()
            .map(MenuProduct::getSeq)
            .collect(Collectors.toList());

        assertThat(menuProductIds).contains(savedMenuProduct.getSeq());
    }

    @DisplayName("MenuId로 MenuProduct들 조회 - 성공")
    @Test
    void findAllByMenuId_Success() {
        Long menuId = getCreatedMenuId();
        Long productId = getCreatedProductId();

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(TEST_QUANTITY);
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        MenuProduct otherMenuProduct = new MenuProduct();
        otherMenuProduct.setMenuId(menuId);
        otherMenuProduct.setProductId(productId);
        otherMenuProduct.setQuantity(TEST_QUANTITY);
        MenuProduct savedOtherMenuProduct = menuProductDao.save(otherMenuProduct);

        MenuProduct menuProductOtherMenu = new MenuProduct();
        menuProductOtherMenu.setMenuId(getCreatedMenuId());
        menuProductOtherMenu.setProductId(productId);
        menuProductOtherMenu.setQuantity(TEST_QUANTITY);
        menuProductDao.save(menuProductOtherMenu);

        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menuId);

        assertThat(menuProducts).hasSize(2);

        List<Long> menuProductIds = menuProducts.stream()
            .filter(menuProduct1 -> menuProduct1.getMenuId().equals(menuId))
            .map(MenuProduct::getSeq)
            .collect(Collectors.toList());

        assertThat(menuProductIds.size()).isEqualTo(menuProducts.size());
        assertThat(menuProductIds).contains(savedMenuProduct.getSeq());
        assertThat(menuProductIds).contains(savedOtherMenuProduct.getSeq());
    }
}
