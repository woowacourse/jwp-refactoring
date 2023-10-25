package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.TableException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class GuestStatusTest {

    @Test
    void 이미_비어있다면_손님_수를_변경할_수_없다() {
        // given
        final GuestStatus guestStatus = new GuestStatus(0, true);

        // expected
        assertThatThrownBy(() -> guestStatus.changeNumberOfGuest(10))
                .isInstanceOf(TableException.TableEmptyException.class);
    }

    @Test
    void 음수로_손님_수를_변경할_수_없다() {
        // given
        final GuestStatus guestStatus = new GuestStatus(10, false);

        // expected
        assertThatThrownBy(() -> guestStatus.changeNumberOfGuest(-1))
                .isInstanceOf(TableException.NoGuestException.class);
    }
}
