package org.chousik.infosec_1.config;

import org.chousik.infosec_1.entity.DataItem;
import org.chousik.infosec_1.entity.UserAccount;
import org.chousik.infosec_1.repository.DataItemRepository;
import org.chousik.infosec_1.repository.UserAccountRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private final DataItemRepository dataRepository;
    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String initialLogin;
    private final String initialPassword;

    public DataInitializer(
            DataItemRepository dataRepository,
            UserAccountRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.initial-user.login}") String initialLogin,
            @Value("${app.initial-user.password}") String initialPassword
    ) {
        this.dataRepository = dataRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.initialLogin = initialLogin;
        this.initialPassword = initialPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsById(initialLogin)) {
            userRepository.save(new UserAccount(
                    initialLogin,
                    passwordEncoder.encode(initialPassword)
            ));
        }

        if (dataRepository.count() == 0) {
            dataRepository.saveAll(List.of(
                    new DataItem("First protected record"),
                    new DataItem("Second protected record")
            ));
        }
    }
}
