package org.chousik.infosec_1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CredentialsRequest(
        @NotBlank
        @Size(min = 4, max = 50)
        @Pattern(regexp = "[A-Za-z0-9_.-]+")
        String login,

        @NotBlank
        @Size(min = 8, max = 72)
        String password
) {
}
