package xyz.luan.acervus.model;

import io.yawp.commons.http.HttpException;
import io.yawp.commons.http.annotation.GET;
import io.yawp.repository.shields.Shield;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.List;

public class UserShield extends Shield<User> {

    @Override
    public void create(List<User> objects) {
        for (User user : objects) {
            if (user.getEmail().trim().isEmpty()) {
                throw new HttpException(422, "Field 'email' is requried.");
            }
            if (!isValidEmailAddress(user.getEmail())) {
                throw new HttpException(422, "Field 'email' must be a valid email.");
            }
            if (user.getPassword().trim().isEmpty()) {
                throw new HttpException(422, "Field 'email' is requried.");
            }
        }
        allow();
    }

    private static boolean isValidEmailAddress(String email) {
        try {
            new InternetAddress(email).validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }

    @GET("renewal")
    public void renewalAll() {
        allow();
    }
}
