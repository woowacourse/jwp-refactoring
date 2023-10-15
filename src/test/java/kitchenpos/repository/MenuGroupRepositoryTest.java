package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MenuGroupRepositoryTest {

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Nested
    class existsById {

        @Test
        void 식별자에_대한_엔티티가_없으면_false() {
            // when
            boolean actual = menuGroupRepository.existsById(1L);

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 식별자에_대한_엔티티가_있으면_true() {
            // given
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName("주류");
            menuGroupRepository.save(menuGroup);

            // when
            boolean actual = menuGroupRepository.existsById(menuGroup.getId());

            assertThat(actual).isTrue();
        }
    }
}
