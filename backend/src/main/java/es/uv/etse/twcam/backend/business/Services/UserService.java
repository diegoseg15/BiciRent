package es.uv.etse.twcam.backend.business.Services;

import es.uv.etse.twcam.backend.business.User;

public class UserService {
    public User login(String email, String password) {
        if ("lupe@gmail.com".equals(email) && "123456".equals(password)) {
            return new User("12345678A", "lupe", "fernandez", "123456789", "lupe@gmail.com", "123456", "usuario");
        }
        return null;
    }
}