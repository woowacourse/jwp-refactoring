package kitchenpos.application.command;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateTableGroupCommandTest extends CommandTest {
    @DisplayName("단체 지정 생성 요청 유효성 검사")
    @Test
    void validation() {
        CreateTableGroupCommand request = new CreateTableGroupCommand(
                asList(1L, 2L));
        CreateTableGroupCommand badRequest = new CreateTableGroupCommand(
                singletonList(1L));

        assertThat(validator.validate(request).isEmpty()).isTrue();
        assertThat(validator.validate(badRequest).size()).isEqualTo(1);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new CreateTableGroupCommand(
                        asList(1L, 1L)));
    }
}