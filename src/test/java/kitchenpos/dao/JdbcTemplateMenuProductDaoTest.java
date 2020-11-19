package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import kitchenpos.domain.MenuProduct;

@JdbcTest
@Import(JdbcTemplateMenuProductDao.class)
class JdbcTemplateMenuProductDaoTest {
    @Autowired
    private JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;

    @DisplayName("MenuProductDao save 테스트")
    @Test
    void save() {
        // Given
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(3L);

        // When
        final MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);

        // Then
        assertThat(savedMenuProduct)
                .extracting(MenuProduct::getSeq)
                .isNotNull()
        ;
    }

    @DisplayName("MenuProduct findById 테스트")
    @Test
    void findById() {
        // When
        final MenuProduct menuProduct = jdbcTemplateMenuProductDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        // Then
        assertAll(
                () -> assertThat(menuProduct)
                        .extracting(MenuProduct::getMenuId)
                        .isEqualTo(1L)
                ,
                () -> assertThat(menuProduct)
                        .extracting(MenuProduct::getProductId)
                        .isEqualTo(1L)
                ,
                () -> assertThat(menuProduct)
                        .extracting(MenuProduct::getQuantity)
                        .isEqualTo(1L)
        );
    }

    @DisplayName("MenuProduct findAll 테스트")
    @Test
    void findAll() {
        // When
        final List<MenuProduct> menuProducts = jdbcTemplateMenuProductDao.findAll();

        // Then
        assertThat(menuProducts).hasSize(6);
    }

    @DisplayName("MenuProduct findAllByMenuId 테스트")
    @Test
    void findAllByMenuId() {
        // When
        final List<MenuProduct> allByMenuId = jdbcTemplateMenuProductDao.findAllByMenuId(2L);

        // Then
        assertThat(allByMenuId).hasSize(1);
    }
}
