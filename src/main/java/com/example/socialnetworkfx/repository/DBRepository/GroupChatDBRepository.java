package com.example.socialnetworkfx.repository.DBRepository;

import com.example.socialnetworkfx.domain.GroupChat;
import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.repository.abstractRepository.AbstractDBRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupChatDBRepository extends AbstractDBRepository<Long, GroupChat> {
    public GroupChatDBRepository(String tableName, String url, String username, String password) {
        super(tableName, url, username, password);
    }

    @Override
    public GroupChat extractEntityFromResultSet(ResultSet attributes) {
        try {
            Long id = attributes.getLong("id");
            String name = attributes.getString("name");
            GroupChat groupChat = new GroupChat(name);
            groupChat.setId(id);
            return groupChat;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement createEntityStatement(GroupChat entity, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into "+super.tableName+" (id, \"name\") values (?,?)");
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getName());
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement updateEntityStatement(GroupChat entity, Connection connection) {
        return null;
    }
}
