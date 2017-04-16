package xyz.luan.acervus.model;

import io.yawp.commons.http.annotation.PUT;
import io.yawp.repository.actions.Action;
import xyz.luan.acervus.Service;

import java.io.IOException;

public class RenewalAction extends Action<User> {

    @PUT
    public void renewalAll() {
        for (User user : yawp(User.class).list()) {
            try {
                Service.renewal(user.getEmail(), user.getPassword());
            } catch (IOException e) {
                System.err.println("Error! Exception thrown!");
                e.printStackTrace();
            }
        }
    }
}
