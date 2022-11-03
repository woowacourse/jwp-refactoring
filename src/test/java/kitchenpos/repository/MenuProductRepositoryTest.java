package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuProductRepositoryTest {

    private final MenuProductRepository menuProductRepository;

    @Autowired
    public MenuProductRepositoryTest(final MenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    @Test
    void 저장한다() {
        // given
        MenuProduct menuProduct = createMenuProduct(1L, 1L, 1L);

        // when
        MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);

        // then
        Assertions.assertAll(
                () -> assertThat(savedMenuProduct.getSeq()).isNotNull(),
                () -> assertThat(savedMenuProduct.getQuantity()).isEqualTo(1),
                () -> assertThat(savedMenuProduct.getProductId()).isEqualTo(1L)
        );
    }

    @Test
    void seq로_조회한다() {
        // given
        Long seq = 1L;

        // when
        Optional<MenuProduct> menuProduct = menuProductRepository.findBySeq(seq);

        // then
        Assertions.assertAll(
                () -> assertThat(menuProduct).isPresent(),
                () -> assertThat(menuProduct.get())
                        .usingRecursiveComparison()
                        .ignoringFields("seq")
                        .isEqualTo(createMenuProduct(0, 1L, 1L))
        );
    }

    @Test
    void 일치하는_seq가_없을_경우_empty를_반환한다() {
        // given
        Long notExistSeq = -1L;

        // when
        Optional<MenuProduct> menuProduct = menuProductRepository.findBySeq(notExistSeq);

        // then
        assertThat(menuProduct).isEmpty();
    }

    @Test
    void 목록을_조회한다() {
        // given & when
        List<MenuProduct> menuProducts = menuProductRepository.findAll();

        // then
        assertThat(menuProducts).hasSize(6)
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(
                        Arrays.asList(
                                createMenuProduct(0, 1L, 1L),
                                createMenuProduct(0, 2L, 1L),
                                createMenuProduct(0, 3L, 1L),
                                createMenuProduct(0, 4L, 1L),
                                createMenuProduct(0, 5L, 1L),
                                createMenuProduct(0, 6L, 1L)
                        )
                );
    }

    private MenuProduct createMenuProduct(final long seq, final long productId, final long quantity) {
        return new MenuProduct(seq, productId, quantity);
    }
}
