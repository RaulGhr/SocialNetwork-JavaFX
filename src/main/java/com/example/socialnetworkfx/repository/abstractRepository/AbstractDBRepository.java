package com.example.socialnetworkfx.repository.abstractRepository;



import com.example.socialnetworkfx.domain.abstractDomain.Entity;
import com.example.socialnetworkfx.repository.paging.Page;
import com.example.socialnetworkfx.repository.paging.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    public String tableName;
    private String url;
    private String username;
    private String password;


    public AbstractDBRepository(String tableName, String url, String username, String password) {
        super();
        this.tableName = tableName;
        this.url = url;
        this.username = username;
        this.password = password;

        loadData();

    }

    private void loadData() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from "+tableName);
             ResultSet resultSet = statement.executeQuery()
        ) {
            super.deleteAll();
            while (resultSet.next())
            {

                E entity = extractEntityFromResultSet(resultSet);
                super.save(entity);

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public abstract E extractEntityFromResultSet(ResultSet attributes);

    protected abstract PreparedStatement createEntityStatement(E entity, Connection connection);
    protected abstract PreparedStatement updateEntityStatement(E entity, Connection connection);

    @Override
    public Optional<E> save(E entity) {

        try (var connection = DriverManager.getConnection(url, username, password))
        {
            PreparedStatement statement= createEntityStatement(entity,connection);
            int response=statement.executeUpdate();
            statement.close();
            return response==0 ? Optional.of(entity) : Optional.empty();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<E> findAll() {
        this.loadData();
        return super.findAll();
    }

    @Override
    public Page<E> findAll(Pageable pageable){
        int numberOfElements = size();
        int limit = pageable.getPageSize();
        int offset = pageable.getPageSize()*pageable.getPageNumber();
//        System.out.println(offset + " ?>= "+numberOfElements);
        if(offset >= numberOfElements)
            return new Page<>(new ArrayList<>(), numberOfElements);

        List<E> entityList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from "+tableName+ " limit ? offset ?");

        ) {
            statement.setInt(2, offset);
            statement.setInt(1,limit);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                E entity = extractEntityFromResultSet(resultSet);
                entityList.add(entity);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        return new Page<>(entityList, numberOfElements);
    }

    @Override
    public Optional<E> findOne(ID id) {
        this.loadData();
        return super.findOne(id);
    }

    @Override
    public Optional<E> delete(ID id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();

        ){
            int rowDeleted = statement.executeUpdate("delete from "+tableName+" where id="+id);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.delete(id);
        return Optional.empty();

    }

    @Override
    public Optional<E> update(E entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = updateEntityStatement(entity,connection);

        ){
            statement.executeUpdate();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Integer size(){
        this.loadData();
        return super.size();
    }


}
