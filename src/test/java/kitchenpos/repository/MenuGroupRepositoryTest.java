package kitchenpos.repository;

import kitchenpos.config.RepositoryTestConfig;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuGroupRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("[EXCEPTION] 메뉴 그룹 식별자값으로 메뉴 그룹을 조회하지 못할 경우 예외가 발생한다.")
    @Test
    void throwException_when_notFound() {
        assertThatThrownBy(() -> menuGroupRepository.findMenuGroupById(-1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
