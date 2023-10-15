package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MenuProductRepositoryTest {

    @Autowired
    MenuProductRepository menuProductRepository;

    @Nested
    class findAllByMenuId {

        @Test
        void menu의_식별자로_모든_엔티티_조회() {
            // given
            Long menuId = 4885L;
            for (long i = 1; i <= 10; i++) {
                MenuProduct menuProduct = new MenuProduct();
                menuProduct.setMenuId(menuId);
                menuProduct.setProductId(i);
                menuProductRepository.save(menuProduct);
            }

            // when
            List<MenuProduct> actual = menuProductRepository.findAllByMenuId(menuId);

            // then
            assertThat(actual).hasSize(10);
        }
    }
}
