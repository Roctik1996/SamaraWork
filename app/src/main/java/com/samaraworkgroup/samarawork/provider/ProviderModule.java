package com.samaraworkgroup.samarawork.provider;

public final class ProviderModule {
    public static PageProviderImpl getUserProvider() {
        return UserProvider.S_USER_PROVIDER;
    }

    private static final class UserProvider {
        private static final PageProviderImpl S_USER_PROVIDER = new PageProviderImpl();
    }
}
