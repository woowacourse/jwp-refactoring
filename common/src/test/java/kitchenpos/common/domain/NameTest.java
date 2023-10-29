package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NameTest {
    @Test
    void throw_when_name_is_empty() {
        // given
        String name = "";

        // when & then
        assertThatThrownBy(() -> Name.of(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Name.NAME_IS_NOT_PROVIDED_ERROR_MESSAGE);
    }

    @Test
    void throw_when_name_is_blank() {
        // given
        String name = " ";

        // when & then
        assertThatThrownBy(() -> Name.of(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Name.NAME_IS_NOT_PROVIDED_ERROR_MESSAGE);
    }

    @Test
    void throw_when_name_is_longer_than_255() {
        // given
        String name = "a".repeat(256);

        // when & then
        assertThatThrownBy(() -> Name.of(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Name.NAME_CANNOT_EXCEED_255_ERROR_MESSAGE);
    }


    @Test
    void throw_when_name_contains_only_numbers() {
        // given
        String name = "1234567890";

        // when & then
        assertThatThrownBy(() -> Name.of(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Name.NAME_CANNOT_CONTAIN_ONLY_NUMBER_ERROR_MESSAGE);
    }

}