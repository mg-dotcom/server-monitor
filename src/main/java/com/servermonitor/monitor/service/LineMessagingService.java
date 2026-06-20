package com.servermonitor.monitor.service;

import com.servermonitor.monitor.dto.line.LineMessageRequest;
import com.servermonitor.monitor.dto.line.ListLineMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
public class LineMessagingService {
    @Value("${line.channel-access-token}")
    private String channelAccessToken;
    private final RestClient restClient;

    public boolean sendMessage(String lineUserId, String message){
        try {
            LineMessageRequest textMessage = LineMessageRequest.builder()
                    .type("text")
                    .text(message)
                    .build();
            ListLineMessageRequest requestBody = ListLineMessageRequest.builder()
                    .to(lineUserId)
                    .messages(List.of(textMessage))
                    .build();
            restClient.post()
                    .uri("https://api.line.me/v2/bot/message/push")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization","Bearer " + channelAccessToken)
                    .body(requestBody)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}