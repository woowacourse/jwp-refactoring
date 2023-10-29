package kitchenpos.menugroup.repository;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("메뉴 그룹 레파지토리 테스트")
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Nested
    class 메뉴_그룹이_존재하는지_확인할_때 {

        @Test
        void 존재한다면_참을_반환한다() {
            // given
            final MenuGroup menuGroup = MenuGroupFixture.메뉴_그룹_엔티티_생성();
            menuGroupRepository.save(menuGroup);

            // when
            final boolean actual = menuGroupRepository.existsById(menuGroup.getId());

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 존재한지_않는다면_거짓을_반환한다() {
            // given
            final long notExistMenuGroupId = 999L;

            // when
            final boolean actual = menuGroupRepository.existsById(notExistMenuGroupId);

            // then
            assertThat(actual).isFalse();
        }
    }
}
