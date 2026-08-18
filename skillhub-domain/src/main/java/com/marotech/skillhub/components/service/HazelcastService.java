package com.marotech.skillhub.components.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.marotech.skillhub.components.config.Config;
import com.marotech.skillhub.model.User;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HazelcastService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HazelcastInstance hazelcastInstance;
    @Autowired
    private Config config;
    private String mapName;

    @PostConstruct
    public void setup(){
        mapName = config.getProperty(MAP_NAME);
    }

    public User getCurrentUser(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            return null;
        }
        IMap map = hazelcastInstance.getMap(mapName);
        String userId = (String) map.get(sessionId);
        if (userId == null) {
            return null;
        }
        return repositoryService.fetchObjectById(User.class, userId);
    }

    public void setCurrentUser(User user, String sessionId) {
        IMap map = hazelcastInstance.getMap(mapName);
        map.put(sessionId, user.getId());
    }

    public static final String MAP_NAME = "app.hazelcast.session.map.name";
}
