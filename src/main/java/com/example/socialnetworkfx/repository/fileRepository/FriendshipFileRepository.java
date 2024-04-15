package com.example.socialnetworkfx.repository.fileRepository;




import com.example.socialnetworkfx.domain.Friendship;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;
import com.example.socialnetworkfx.formats.TimeFormat;
import com.example.socialnetworkfx.repository.abstractRepository.AbstractFileRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Tuple<Long, Long>, Friendship> {




    public FriendshipFileRepository(String fileName) {
        super(fileName);

    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        DateTimeFormatter formatter = TimeFormat.dateFormatter();
        Friendship friendship = new Friendship(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1)));
        friendship.setDate(LocalDateTime.parse(attributes.get(2), formatter));

        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        DateTimeFormatter formatter = TimeFormat.dateFormatter();
        return entity.getId().getLeft() + ";" + entity.getId().getRight() + ";" + entity.getDate().format(formatter);
    }
}
