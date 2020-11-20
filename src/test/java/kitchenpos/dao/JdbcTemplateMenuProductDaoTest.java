package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.MenuProduct;

@DisplayName("JdbcTemplateMenuProductDao 테스트")
@Sql("/dao-test.sql")
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
        assertAll(
                () -> assertThat(savedMenuProduct)
                        .extracting(MenuProduct::getSeq)
                        .isNotNull()
                ,
                () -> assertThat(savedMenuProduct)
                        .extracting(MenuProduct::getMenuId)
                        .isEqualTo(menuProduct.getMenuId())
                ,
                () -> assertThat(savedMenuProduct)
                        .extracting(MenuProduct::getProductId)
                        .isEqualTo(menuProduct.getProductId())
                ,
                () -> assertThat(savedMenuProduct)
                        .extracting(MenuProduct::getQuantity)
                        .isEqualTo(menuProduct.getQuantity())
        );
    }

    @DisplayName("MenuProductDao findById 테스트")
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

    @DisplayName("MenuProductDao findById Id가 존재하지 않을 경우")
    @Test
    void findById_NotExists() {
        // When
        final Optional<MenuProduct> menuProduct = jdbcTemplateMenuProductDao.findById(7L);

        // Then
        assertThat(menuProduct.isPresent()).isFalse()
        ;
    }

    @DisplayName("MenuProductDao findAll 테스트")
    @Test
    void findAll() {
        // When
        final List<MenuProduct> menuProducts = jdbcTemplateMenuProductDao.findAll();

        // Then
        assertThat(menuProducts).hasSize(6);
    }

    @DisplayName("MenuProductDao findAllByMenuId 테스트")
    @Test
    void findAllByMenuId() {
        // When
        final List<MenuProduct> allByMenuId = jdbcTemplateMenuProductDao.findAllByMenuId(2L);

        // Then
        assertThat(allByMenuId).hasSize(1);
    }
}
