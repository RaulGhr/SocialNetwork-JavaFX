package com.example.socialnetworkfx.repository.DBRepository;




import com.example.socialnetworkfx.domain.Friendship;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;
import com.example.socialnetworkfx.repository.abstractRepository.AbstractDBRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipDBRepository extends AbstractDBRepository<Tuple<Long, Long>, Friendship> {

    public FriendshipDBRepository(String tableName, String url, String username, String password) {
        super(tableName, url, username, password);
    }

    @Override
    public Friendship extractEntityFromResultSet(ResultSet attributes) {
        try {


            Friendship friendship = new Friendship(attributes.getLong("id1"), attributes.getLong("id2"));
            friendship.setDate(attributes.getTimestamp("friendsFrom").toLocalDateTime());
            return friendship;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected PreparedStatement createEntityStatement(Friendship entity, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into friendships (id1,id2,\"friendsFrom\") values (?,?,?)");
            statement.setLong(1, entity.getId().getLeft());
            statement.setLong(2, entity.getId().getRight());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(entity.getDate()));
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement updateEntityStatement(Friendship entity, Connection connection) {

        throw new RuntimeException("Friendship cannot be updated!");

    }
}

