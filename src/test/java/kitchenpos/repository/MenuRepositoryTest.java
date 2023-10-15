package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MenuRepositoryTest {

    @Autowired
    MenuRepository menuRepository;

    @Nested
    class countByIdIn {

        @Test
        void 식별자_목록으로_개수_조회() {
            // given
            List<Long> ids = List.of(1L, 2L, 3L);
            for (int i = 0; i < 3; i++) {
                Menu menu = new Menu();
                menu.setPrice(BigDecimal.ZERO);
                menu.setName("menu" + i);
                menu.setMenuGroupId(1L);
                menuRepository.save(menu);
            }

            // when
            long actual = menuRepository.countByIdIn(ids);

            // then
            assertThat(actual).isEqualTo(3);
        }
    }
}
