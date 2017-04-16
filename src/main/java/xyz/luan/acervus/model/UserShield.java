package xyz.luan.acervus.model;

import io.yawp.repository.shields.Shield;

import java.util.List;

public class UserShield extends Shield<User> {

    @Override
    public void create(List<User> objects) {
        allow();
    }
}
