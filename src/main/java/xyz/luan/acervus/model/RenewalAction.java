package xyz.luan.acervus.model;

import io.yawp.commons.http.annotation.PUT;
import io.yawp.repository.actions.Action;
import xyz.luan.acervus.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RenewalAction extends Action<User> {

    private final static Logger LOGGER = Logger.getLogger(RenewalAction.class.getName());

    @PUT("renewal")
    public List<String> renewalAll() {
        List<String> results = new ArrayList<>();
        for (User user : yawp(User.class).list()) {
            try {
                String result = Service.renewal(user.getEmail(), user.getPassword());
                results.add("User " + user.getEmail() + ": " + result);
            } catch (IOException e) {
                results.add("User " + user.getEmail() + " failed catastrophically.");
                System.err.println("Error! Exception thrown!");
                e.printStackTrace();
            }
        }
        LOGGER.info(results.toString());
        return results;
    }
}
