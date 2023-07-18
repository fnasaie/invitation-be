package com.naqi.invitation.controller;

import com.naqi.invitation.models.Message;
import com.naqi.invitation.repositories.MessageRepository;
import com.naqi.invitation.services.MessageService;
import com.naqi.invitation.utility.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // @GetMapping("/getMessage")
    // public ResponseEntity<HttpResponse> getAllMessages(HttpServletRequest request) {
    //     HttpResponse response = new HttpResponse(request.getRequestURI());

    //     try {
    //         Page<Message> messagePage = messageService.getMessagesAfterId(0, 10);
    //         response.setStatus(HttpStatus.OK);
    //         response.setData(messagePage.getContent());
    //     } catch (Exception e) {
    //         response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    //         response.setMessage("Unexpected error getting messages: " + e.getMessage());
    //     }
    //     return ResponseEntity.status(response.getStatus()).body(response);
    // }

    @GetMapping("/{lastMessageId}")
    public ResponseEntity<HttpResponse> getMessagesAfterId(HttpServletRequest request,
                                                          @PathVariable int lastMessageId) {
        HttpResponse response = new HttpResponse(request.getRequestURI());

        try {
            Page<Message> messagePage = messageService.getMessagesAfterId(lastMessageId, 10);
            response.setStatus(HttpStatus.OK);
            response.setData(messagePage.getContent());
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Unexpected error getting messages: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<HttpResponse> createMessage(HttpServletRequest request, @RequestBody Message message) {
        HttpResponse response = new HttpResponse(request.getRequestURI());

        try {
            messageService.createMessage(message);

            List<Message> messageList = messageRepository.findAll(Sort.by(Sort.Direction.ASC, "created"));
            response.setStatus(HttpStatus.OK);
            response.setData(messageList);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Unexpected error creating message: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getMessage")
    public ResponseEntity<HttpResponse> getAllMessages(HttpServletRequest request) {
        HttpResponse response = new HttpResponse(request.getRequestURI());

        try {
            List<Message> messageList = messageRepository.findAll(Sort.by(Sort.Direction.ASC, "created"));
            response.setStatus(HttpStatus.OK);
            response.setData(messageList);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Unexpected error getting messages: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
