package org.example.chatting.controller;

import org.example.chatting.dto.MessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private static final List<String> users = new ArrayList<>();

    @GetMapping("/chatroom")
    public String getChatRoom(Model model) {
        model.addAttribute("users", users); // 채팅방에 있는 사용자 목록을 모델에 전달
        return "chatroom";
    }

    // 메시지 전송
    @MessageMapping("/chat") // "/app/chat" 경로로 들어오는 요청을 처리
    @SendTo("/topic/messages") // "/topic/messages"로 메시지를 브로드캐스트
    public String sendMessage(@Payload MessageDto messageDto) {
        return messageDto.getUserId() + ": " + messageDto.getContent();
    }

    // 채팅방 입장
    @MessageMapping("/enter") // "/app/enter" 경로로 들어오는 요청을 처리
    @SendTo("/topic/messages")
    public String userEnter(@Payload MessageDto messageDto) {
        String userId = messageDto.getUserId();
        if (!users.contains(userId)) {
            users.add(userId);
        }
        return userId + "님이 채팅방에 입장했습니다.";
    }

    // 채팅방 퇴장
    @MessageMapping("/exit") // "/app/exit" 경로로 들어오는 요청을 처리
    @SendTo("/topic/messages")
    public String userExit(@Payload String userId) {
        users.remove(userId);
        return userId + "님이 채팅방을 나갔습니다.";
    }
}
