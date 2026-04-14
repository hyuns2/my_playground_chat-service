package io.playground.chatservice.application.chat.manager;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {
    private final Map<String, Set<String>> userDevices = new ConcurrentHashMap<>();
    private final Map<String, Set<Long>> userRooms = new ConcurrentHashMap<>();
    private final Map<Long, Set<String>> participants = new ConcurrentHashMap<>();

    // ToDo: 그럼 유저의 방이 늘어나거나 줄면? 참가자는??
    public Set<Long> addSession(String userId, String deviceId, List<Long> chatRoomIds) {
        userDevices
                .computeIfAbsent(userId, k -> new HashSet<>())
                .add(deviceId);

        userRooms.put(userId, Set.copyOf(chatRoomIds));

        Set<Long> newRoomIds = new HashSet<>();
        for (Long roomId : chatRoomIds) {
            if (!participants.containsKey(roomId)) {
                newRoomIds.add(roomId);
                participants.put(roomId, new HashSet<>());
            }

            participants.get(roomId).add(userId);
        }

        return newRoomIds;
    }

    public Set<Long> removeSession(String userId, String deviceId) {
        userDevices.get(userId).remove(deviceId);

        if (!userDevices.get(userId).isEmpty())
            return null;

        Set<Long> emptyRoomIds = new HashSet<>();
        userDevices.remove(userId);
        for (Long roomId : userRooms.remove(userId)) {
            participants.get(roomId).remove(userId);
            if (participants.get(roomId).isEmpty())
                emptyRoomIds.add(roomId);
        }

        return emptyRoomIds;
    }

    public boolean hasRoom(Long roomId) {
        return participants.containsKey(roomId);
    }
}
