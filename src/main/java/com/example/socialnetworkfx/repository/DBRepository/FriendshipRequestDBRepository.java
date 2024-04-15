package com.example.socialnetworkfx.repository.DBRepository;

import com.example.socialnetworkfx.domain.Friendship;
import com.example.socialnetworkfx.domain.FriendshipRequest;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;
import com.example.socialnetworkfx.repository.abstractRepository.AbstractDBRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipRequestDBRepository extends AbstractDBRepository<Tuple<Long, Long>, FriendshipRequest> {
    public FriendshipRequestDBRepository(String tableName, String url, String username, String password) {
        super(tableName, url, username, password);
    }

    @Override
    public FriendshipRequest extractEntityFromResultSet(ResultSet attributes) {
        try {
            FriendshipRequest friendshipRequest = new FriendshipRequest(attributes.getLong("idSender"), attributes.getLong("idReceiver"), attributes.getLong("status"));
            friendshipRequest.setDate(attributes.getTimestamp("date").toLocalDateTime());
            return friendshipRequest;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement createEntityStatement(FriendshipRequest entity, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into "+super.tableName+" (\"idSender\",\"idReceiver\",\"status\",\"date\") values (?,?,?,?)");
            statement.setLong(1, entity.getIdSender());
            statement.setLong(2, entity.getIdReceiver());
            statement.setLong(3, entity.getStatus());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(entity.getDate()));
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement updateEntityStatement(FriendshipRequest entity, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("update "+super.tableName+" set status=? where \"idSender\"=? and \"idReceiver\"=?");
            statement.setLong(1, entity.getStatus());
            statement.setLong(2, entity.getIdSender());
            statement.setLong(3, entity.getIdReceiver());
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
