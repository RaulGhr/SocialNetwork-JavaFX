package com.example.socialnetworkfx.repository.abstractRepository;


import com.example.socialnetworkfx.domain.abstractDomain.Entity;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;

    public AbstractFileRepository(String fileName) {
        super();
        this.fileName = fileName;
        loadData();

    }

    private void loadData() { //decorator pattern
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String newLine;
            while ((newLine = reader.readLine()) != null) {
//                System.out.println(newLine);
                List<String> data = Arrays.asList(newLine.split(";"));
                E entity = extractEntity(data);
                super.save(entity);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Path path= Paths.get(fileName);
//        try{
//            List<String> lines= Files.readAllLines(path);
//            lines.forEach(line -> {
//                E entity=extractEntity(Arrays.asList(line.split(";")));
//                super.save(entity);
//            });
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }

    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);  //Template Method


    protected abstract String createEntityAsString(E entity); //Template Method

    @Override
    public Optional<E> save(E entity) {
        Optional<E> result = super.save(entity);
        if (result.isEmpty())
            writeToFile();
        return result;
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> result = super.delete(id);
        if (result.isPresent())
            writeToFile();
        return result;
    }

    protected void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

//            for (E entity : findAll()) {
//                writer.write(createEntityAsString(entity));
//                writer.newLine();
//            }
            findAll().forEach(entity -> {
                try {
                    writer.write(createEntityAsString(entity));
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

