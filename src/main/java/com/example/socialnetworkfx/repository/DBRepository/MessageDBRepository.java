package com.example.socialnetworkfx.repository.DBRepository;

import com.example.socialnetworkfx.domain.GroupChat;
import com.example.socialnetworkfx.domain.Message;
import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.repository.abstractRepository.AbstractDBRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MessageDBRepository extends AbstractDBRepository<Long, Message> {
    public MessageDBRepository(String tableName, String url, String username, String password) {
        super(tableName, url, username, password);
    }

    @Override
    public Message extractEntityFromResultSet(ResultSet attributes) {
        try {
            Long id = attributes.getLong("id");
            Long idGroupChat = attributes.getLong("idGroupChat");
            Long idUser = attributes.getLong("idUser");
            String text = attributes.getString("text");
            Long replyTo = null;
            if(attributes.getLong("replyTo") != 0)
                replyTo = attributes.getLong("replyTo");
            LocalDateTime date = attributes.getTimestamp("date").toLocalDateTime();
            Message message = new Message(idGroupChat, idUser, text, replyTo);
            message.setDate(date);
            message.setId(id);
            return message;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement createEntityStatement(Message entity, Connection connection) {
        try {
            PreparedStatement statement;
            if(entity.getReplyTo() == null) {
                statement = connection.prepareStatement("insert into " + super.tableName + " (id,\"idGroupChat\",\"idUser\",\"text\",\"date\") values (?,?,?,?,?)");
                statement.setLong(1, entity.getId());
                statement.setLong(2, entity.getIdGroupChat());
                statement.setLong(3, entity.getIdUser());
                statement.setString(4, entity.getText());
                statement.setTimestamp(5, java.sql.Timestamp.valueOf(entity.getDate()));
            }
            else {
                statement = connection.prepareStatement("insert into " + super.tableName + " (id,\"idGroupChat\",\"idUser\",\"text\",\"replyTo\",\"date\") values (?,?,?,?,?,?)");
                statement.setLong(1, entity.getId());
                statement.setLong(2, entity.getIdGroupChat());
                statement.setLong(3, entity.getIdUser());
                statement.setString(4, entity.getText());
                statement.setLong(5, entity.getReplyTo());
                statement.setTimestamp(6, java.sql.Timestamp.valueOf(entity.getDate()));
            }




            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement updateEntityStatement(Message entity, Connection connection) {
        return null;
    }
}
