package com.ada.moviesbattle.security.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static String getLoggedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
