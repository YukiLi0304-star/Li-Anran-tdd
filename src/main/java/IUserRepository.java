package main.java;

public interface IUserRepository {
    User findById(String userId);
    void save(User user);
}
