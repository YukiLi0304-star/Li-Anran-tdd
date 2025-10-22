package main.java;

import java.util.*;
public class MemoryUserRepository implements IUserRepository{
    private final Map<String, User> users = new HashMap<>();

    @Override
    public User findById(String userId){
        return users.get(userId);
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }
}
