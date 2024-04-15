package com.example.socialnetworkfx.repository.DBRepository;

import com.example.socialnetworkfx.domain.Friendship;
import com.example.socialnetworkfx.domain.GroupChatMember;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;
import com.example.socialnetworkfx.repository.abstractRepository.AbstractDBRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupChatMemberDBRepository extends AbstractDBRepository<Tuple<Long, Long>, GroupChatMember> {
    public GroupChatMemberDBRepository(String tableName, String url, String username, String password) {
        super(tableName, url, username, password);
    }

    @Override
    public GroupChatMember extractEntityFromResultSet(ResultSet attributes) {
        try {
            GroupChatMember groupChatMember = new GroupChatMember(attributes.getLong("idUser"), attributes.getLong("idGroup"));
            return groupChatMember;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement createEntityStatement(GroupChatMember entity, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into "+super.tableName+" (\"idUser\",\"idGroup\") values (?,?)");
            statement.setLong(1, entity.getIdUser());
            statement.setLong(2, entity.getIdGroup());
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement updateEntityStatement(GroupChatMember entity, Connection connection) {
        return null;
    }


}
