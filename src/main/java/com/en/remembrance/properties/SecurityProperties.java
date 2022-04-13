package com.en.remembrance.properties;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SecurityProperties {

    private boolean enabled;

    private int emailVerificationExpiryDays;

    private int forgotPasswordExpiryDays;

    private int sharedStorybooksExpiryDays;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
