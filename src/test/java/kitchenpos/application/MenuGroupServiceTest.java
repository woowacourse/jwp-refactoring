package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFactory.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    MenuGroupService sut;

    @Test
    @DisplayName("MenuGroup을 생성한다")
    void delegateSaveAndReturnSavedEntity() {
        // given
        var request = createMenuGroupRequest();

        // when
        var actual = sut.create(request);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("MenuGroup 목록을 조회한다")
    void returnAllSavedEntities() {
        var expected = menuGroupRepository.findAll();

        var actual = sut.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
