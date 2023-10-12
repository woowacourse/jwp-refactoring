package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class MenuProductDaoTest {
    @Autowired
    private DataSource dataSource;
    private MenuProductDao menuProductDao;

    @BeforeEach
    void setUp() {
        menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
    }

    @Test
    @DisplayName("메뉴 상품을 저장한다.")
    public void save() {
        //given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(3L);

        //when
        MenuProduct returnedMenuProduct = menuProductDao.save(menuProduct);

        //then
        assertThat(returnedMenuProduct.getSeq()).isNotNull();
        assertThat(menuProduct.getMenuId()).isEqualTo(returnedMenuProduct.getMenuId());
        assertThat(menuProduct.getProductId()).isEqualTo(returnedMenuProduct.getProductId());
        assertThat(menuProduct.getQuantity()).isEqualTo(returnedMenuProduct.getQuantity());
    }

    @Test
    @DisplayName("메뉴 상품을 아이디로 조회한다.")
    public void findById() {
        //given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(3L);
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        //when
        Optional<MenuProduct> returnedMenuProduct = menuProductDao.findById(savedMenuProduct.getSeq());

        //then
        assertThat(returnedMenuProduct).isPresent();
        assertThat(savedMenuProduct.getSeq()).isEqualTo(returnedMenuProduct.get().getSeq());
        assertThat(savedMenuProduct.getMenuId()).isEqualTo(returnedMenuProduct.get().getMenuId());
        assertThat(savedMenuProduct.getProductId()).isEqualTo(returnedMenuProduct.get().getProductId());
        assertThat(savedMenuProduct.getQuantity()).isEqualTo(returnedMenuProduct.get().getQuantity());
    }

    @Test
    @DisplayName("모든 메뉴 상품을 조회한다.")
    public void findAll() {
        //given
        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setMenuId(1L);
        menuProduct1.setProductId(2L);
        menuProduct1.setQuantity(3L);
        MenuProduct savedMenuProduct1 = menuProductDao.save(menuProduct1);

        MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setMenuId(2L);
        menuProduct2.setProductId(3L);
        menuProduct2.setQuantity(4L);
        MenuProduct savedMenuProduct2 = menuProductDao.save(menuProduct2);

        //when
        List<MenuProduct> menuProducts = menuProductDao.findAll();

        //then
        assertThat(menuProducts).contains(savedMenuProduct1, savedMenuProduct2);
    }

    @Test
    @DisplayName("메뉴 아이디로 메뉴 상품을 조회한다.")
    public void findAllByMenuId() {
        //given
        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setMenuId(1L);
        menuProduct1.setProductId(2L);
        menuProduct1.setQuantity(3L);
        MenuProduct savedMenuProduct1 = menuProductDao.save(menuProduct1);

        MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setMenuId(1L);
        menuProduct2.setProductId(3L);
        menuProduct2.setQuantity(4L);
        MenuProduct savedMenuProduct2 = menuProductDao.save(menuProduct2);

        //when
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(1L);

        //then
        assertThat(menuProducts).contains(savedMenuProduct1, savedMenuProduct2);
    }
}
