package com.lades.websocket.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void saveUser(User user){
        user.setStatus(Status.ONLINE);
        userRepository.save(user);

    }

    public void disconnectUser(User user){
        var userTobeDisconnected = userRepository.findById(user.getNickName()).orElse(null);
        if (userTobeDisconnected != null){
            userTobeDisconnected.setStatus(Status.OFFLINE);
            userRepository.save(userTobeDisconnected);
        }

    }

    public List<User> findAllConnectedUsers(){
        return userRepository.findAllByStatus(Status.ONLINE);
    }
}
