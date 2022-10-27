package kitchenpos.dao;

import static kitchenpos.fixture.MenuBuilder.aMenu;
import static kitchenpos.fixture.MenuGroupFactory.createMenuGroup;
import static kitchenpos.fixture.ProductBuilder.aProduct;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class MenuProductDaoTest {

    @Autowired
    DataSource dataSource;

    MenuProductDao sut;
    MenuDao menuDao;
    ProductDao productDao;
    MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        sut = new JdbcTemplateMenuProductDao(dataSource);
        menuDao = new JdbcTemplateMenuDao(dataSource);
        productDao = new JdbcTemplateProductDao(dataSource);
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("MenuProduct를 저장하고 저장된 엔티티를 반환한다")
    void save() {
        // given
        MenuProduct menuProduct = new MenuProduct(null, 1L, 1L, 1L);

        // when
        MenuProduct savedMenuProduct = sut.save(menuProduct);

        // then
        MenuProduct findMenuProduct = sut.findById(savedMenuProduct.getSeq()).get();
        assertThat(savedMenuProduct).isEqualTo(findMenuProduct);
    }

    @Test
    @DisplayName("존재하지 않는 id를 입력받으면 빈 객체를 반환한다")
    void returnOptionalEmpty_WhenIdDoesNotExist() {
        Optional<MenuProduct> actual = sut.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("저장된 모든 MenuProduct를 조회한다")
    void findAll() {
        // given
        List<MenuProduct> previousSaved = sut.findAll();

        MenuProduct menuProduct = new MenuProduct(null, 1L, 1L, 1L);

        sut.save(menuProduct);

        // when
        List<MenuProduct> result = sut.findAll();

        // then
        assertThat(result.size()).isEqualTo(previousSaved.size() + 1);
    }

    @Test
    @DisplayName("입력받은 Menu Id와 연관된 MenuProduct 목록을 조회한다")
    void findAllByMenuId() {
        // given
        Long menuId = menuDao.save(
                aMenu(savedMenuGroup().getId())
                        .build()
        ).getId();

        Long productId = productDao.save(aProduct().build()).getId();

        MenuProduct menuProduct = new MenuProduct(null, menuId, productId, 1L);
        MenuProduct savedMenuProduct = sut.save(menuProduct);

        // when
        List<MenuProduct> actual = sut.findAllByMenuId(menuId);

        // then
        assertThat(actual).isEqualTo(List.of(savedMenuProduct));
    }

    private MenuGroup savedMenuGroup() {
        return menuGroupDao.save(createMenuGroup());
    }
}
