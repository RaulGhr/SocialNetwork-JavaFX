package com.example.socialnetworkfx.repository.DBRepository;



import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.repository.abstractRepository.AbstractDBRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBRepository extends AbstractDBRepository<Long, User> {

    public UserDBRepository(String tableName, String url, String username, String password) {
        super(tableName, url, username, password);
    }

    @Override
    public User extractEntityFromResultSet(ResultSet attributes) {
       try {


           Long id = attributes.getLong("id");
           String firstName = attributes.getString("firstName");
           String lastName = attributes.getString("lastName");
           String email = attributes.getString("email");
           String password = attributes.getString("password");
           User user = new User(firstName, lastName, email, password);
           user.setId(id);
           return user;

       }catch (SQLException e) {
           throw new RuntimeException(e);
       }

    }

    @Override
    protected PreparedStatement createEntityStatement(User entity, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into users (id, \"firstName\",\"lastName\",\"email\", \"password\") values (?,?,?,?,?)");
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getPassword());
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement updateEntityStatement(User entity, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("update users set \"firstName\"=?, \"lastName\"=?,\"email\"=?, \"password\"=? where id=?");
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setLong(5, entity.getId());
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
