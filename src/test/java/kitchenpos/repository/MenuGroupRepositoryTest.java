package kitchenpos.repository;

import static kitchenpos.support.DomainFixture.세트_메뉴;
import static kitchenpos.support.DomainFixture.인기_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.repository.menu.MenuGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("classpath:truncate.sql")
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_그룹을_저장한다() {
        // when
        final var saved = menuGroupRepository.save(인기_메뉴);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo(인기_메뉴.getName())
        );
    }

    @Test
    void ID로_메뉴_그룹을_조회한다() {
        // given
        final var saved = menuGroupRepository.save(인기_메뉴);

        // when
        final var found = menuGroupRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getId()).isEqualTo(saved.getId());
    }
    
    @Test
    void 모든_메뉴_그룹을_조회한다() {
        // given
        menuGroupRepository.save(세트_메뉴);
        menuGroupRepository.save(인기_메뉴);

        // when
        final var found = menuGroupRepository.findAll();

        // then
        assertThat(found).hasSize(2);
    }
    
    @Test
    void ID로_메뉴_그룹이_존재하는지_확인한다_있음() {
        // given
        final var saved = menuGroupRepository.save(인기_메뉴);

        // when
        final var exists = menuGroupRepository.existsById(saved.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void ID로_메뉴_그룹이_존재하는지_확인한다_없음() {
        // when
        final var exists = menuGroupRepository.existsById(0L);

        // then
        assertThat(exists).isFalse();
    }
}
