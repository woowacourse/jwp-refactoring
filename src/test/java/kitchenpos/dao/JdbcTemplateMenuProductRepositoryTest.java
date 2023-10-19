package kitchenpos.dao;

import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.fixture.MenuProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateMenuProductRepositoryTest {

    @Autowired
    private JdbcTemplateMenuProductRepository menuProductDao;

    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuProduct = MenuProductFixture.메뉴_재고(1L, 1L, 3);
    }

    @Test
    void 메뉴에_상품을_등록한다() {
        // when
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        // then
        assertThat(savedMenuProduct).usingRecursiveComparison()
                .ignoringFields("id", "seq")
                .isEqualTo(menuProduct);
    }

    @Test
    void 상품이_있는_메뉴의_id로_상품이_있는_메뉴를_조회한다() {
        // given
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        // when
        MenuProduct foundMenuProduct = menuProductDao.findById(savedMenuProduct.getSeq()).get();

        // then
        assertThat(foundMenuProduct).usingRecursiveComparison()
                .ignoringFields("id", "seq")
                .isEqualTo(menuProduct);
    }

    @Test
    void 상품이_있는_메뉴의_목록을_조회한다() {
        // given
        int originSize = menuProductDao.findAll().size();

        // when
        menuProductDao.save(menuProduct);
        List<MenuProduct> menuProducts = menuProductDao.findAll();

        // then
        assertThat(menuProducts).hasSize(originSize + 1);
    }

    @Test
    void 메뉴id로_상품이_있는_메뉴를_조회한다() {
        // given
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        // when
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(savedMenuProduct.getMenuId());

        // then
        assertThat(menuProducts).hasSize(2);
    }
}
