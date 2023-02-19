package org.unicrm.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicrm.chat.dto.GroupDto;
import org.unicrm.chat.dto.GroupListDto;
import org.unicrm.chat.model.ListNewUsersGroup;
import org.unicrm.chat.service.GroupService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GroupController {
    private final GroupService groupService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/newgroup")
    public void createNewGroup(@Payload ListNewUsersGroup dto,
                               @Header(name = "simpSessionId") String sessionId) {

        groupService.createGroup(dto);
        messagingTemplate.convertAndSend("/queue/" + sessionId, dto);
    }
    @MessageMapping("/groups")
    public void findAllGroup(@Payload GroupListDto dto,
                                 @Header(name = "simpSessionId") String sessionId) {
        GroupListDto dto1 = GroupListDto.builder()
                .type(dto.getType())
                .groups(groupService.findAll())
                .build();
        messagingTemplate.convertAndSend("/queue/" + sessionId, dto1);
    }
    @MessageMapping("/newusers")
    public void addNewUsersInGroup(@Payload ListNewUsersGroup dto,
                                                @Header(name = "simpSessionId") String sessionId) {
        groupService.addUsers(dto);


    }

}
