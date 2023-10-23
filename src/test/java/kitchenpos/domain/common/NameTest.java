package kitchenpos.domain.common;

import kitchenpos.domain.DomainTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class NameTest extends DomainTest {
    @Test
    void throw_when_name_is_null() {
        // given
        String name = null;

        // when & then
        assertThrows(IllegalArgumentException.class, () -> Name.of(name));
    }

    @Test
    void throw_when_name_is_empty() {
        // given
        String name = "";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> Name.of(name));
    }

    @Test
    void throw_when_name_is_blank() {
        // given
        String name = " ";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> Name.of(name));
    }

    @Test
    void throw_when_name_is_longer_than_255() {
        // given
        String name = "a".repeat(256);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> Name.of(name));
    }

    @Test
    void throw_when_name_contains_special_characters() {
        // given
        String name = "a!@#$%^&*()_+-=[]{}\\|;':\",./<>?";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> Name.of(name));
    }

    @Test
    void throw_when_name_contains_only_numbers() {
        // given
        String name = "1234567890";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> Name.of(name));
    }

}