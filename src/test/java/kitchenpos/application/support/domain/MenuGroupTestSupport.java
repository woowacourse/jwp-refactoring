package kitchenpos.application.support.domain;

import java.util.UUID;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.domain.MenuGroup;

public class MenuGroupTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name = "허브 잔뜩 들어간 메뉴 그룹" + UUID.randomUUID().toString().substring(0, 5);

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(name);
        }

        public MenuGroupCreateRequest buildToMenuGroupCreateRequest() {
            return new MenuGroupCreateRequest(name);
        }
    }
}
