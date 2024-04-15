package com.example.socialnetworkfx.service;



import com.example.socialnetworkfx.domain.Friendship;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;
import com.example.socialnetworkfx.repository.abstractRepository.Repository;

import java.util.*;

public class NetworkService {
    Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository;
    public NetworkService(Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository){
        this.friendshipFileRepository = friendshipFileRepository;
    }

    public List<Set<Long>> allNetwork(){
        Iterable<Friendship> friendships = friendshipFileRepository.findAll();
        ConnectedComponentsFinder connectedComponentsFinder = new ConnectedComponentsFinder();
        return connectedComponentsFinder.findConnectedComponents(friendships);
    }

    public Integer numberOfCommunities(){
        return allNetwork().size();
    }

    public Set<Long> biggestCommunity(){
        List<Set<Long>> connectedComponents = allNetwork();
//        Set<Long> biggestCommunity = new HashSet<>();
//        for(Set<Long> connectedComponent : connectedComponents){
//            if(connectedComponent.size() > biggestCommunity.size())
//                biggestCommunity = connectedComponent;
//        }

        Optional<Set<Long>>  biggestCommunity = connectedComponents.stream().reduce(
                (set1, set2) -> set1.size() > set2.size()?set1:set2);


        return biggestCommunity.get();
    }


}

class ConnectedComponentsFinder{
    private Map<Long, List<Long>> adjacencyList = new HashMap<>();
    private Set<Long> visited = new HashSet<>();
    private List<Set<Long>> connectedComponents = new ArrayList<>();

    public List<Set<Long>> findConnectedComponents(Iterable<Friendship> friendships){
//        for(Friendship friendship : friendships){
//            Long id1 = friendship.getId().getLeft();
//            Long id2 = friendship.getId().getRight();
//            if(!adjacencyList.containsKey(id1))
//                adjacencyList.put(id1,new ArrayList<>());
//            if(!adjacencyList.containsKey(id2))
//                adjacencyList.put(id2,new ArrayList<>());
//            adjacencyList.get(id1).add(id2);
//            adjacencyList.get(id2).add(id1);
//        }
//        for(Long id : adjacencyList.keySet()){
//            if(!visited.contains(id)){
//                Set<Long> connectedComponent = new HashSet<>();
//                dfs(id,connectedComponent);
//                connectedComponents.add(connectedComponent);
//            }
//        }
        friendships.forEach(friendship -> {
            Long id1 = friendship.getId().getLeft();
            Long id2 = friendship.getId().getRight();
            if(!adjacencyList.containsKey(id1))
                adjacencyList.put(id1,new ArrayList<>());
            if(!adjacencyList.containsKey(id2))
                adjacencyList.put(id2,new ArrayList<>());
            adjacencyList.get(id1).add(id2);
            adjacencyList.get(id2).add(id1);
        });

        adjacencyList.keySet().forEach(id -> {
            if(!visited.contains(id)){
                Set<Long> connectedComponent = new HashSet<>();
                dfs(id,connectedComponent);
                connectedComponents.add(connectedComponent);
            }
        });
        return connectedComponents;
    }

    private void dfs(Long id, Set<Long> connectedComponent){
        visited.add(id);
        connectedComponent.add(id);
//        for(Long neighbour : adjacencyList.get(id)){
//            if(!visited.contains(neighbour))
//                dfs(neighbour,connectedComponent);
//        }
        adjacencyList.get(id).forEach(neighbour -> {
            if(!visited.contains(neighbour))
                dfs(neighbour,connectedComponent);
        });
    }

}
