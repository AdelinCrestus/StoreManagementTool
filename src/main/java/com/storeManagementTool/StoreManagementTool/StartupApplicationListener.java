package com.storeManagementTool.StoreManagementTool;

import com.storeManagementTool.StoreManagementTool.entities.CartEntity;
import com.storeManagementTool.StoreManagementTool.entities.Role;
import com.storeManagementTool.StoreManagementTool.entities.UserEntity;
import com.storeManagementTool.StoreManagementTool.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StartupApplicationListener {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        checkAndCreateAdminUser();
    }

    private void checkAndCreateAdminUser() {
        UserEntity existingAdmin = userRepository.findByUsername("AdminUser").orElse(null);
        if (existingAdmin == null) {
            UserEntity newAdmin = new UserEntity();
            newAdmin.setUsername("AdminUser");
            newAdmin.setEmail("admin@email.com");
            newAdmin.setPassword(passwordEncoder.encode("admin"));
            newAdmin.setRole(Role.ADMIN);
            newAdmin.setCart(new CartEntity());
            userRepository.save(newAdmin);
        }
    }
}
