package com.project.webapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webapp.entity.Assignment;
import com.project.webapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class SnsService {
    @Value("${snsTopicArn}")
    String snsTopicArn;

    @Autowired
    private SnsClient snsClient;
    String jsonString;

    public SnsService() {
    }

    public void publishMessage(String message) {
        PublishRequest request = PublishRequest.builder()
                .topicArn(snsTopicArn)
                .message(message)
                .build();
        snsClient.publish(request);
    }

    public String buildJson(String valid, User user, Assignment assignment, int attempt,String submission_url) {

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("valid",valid);
        jsonMap.put("email", user.getEmail());
        jsonMap.put("assignmentid", assignment.getId());
        jsonMap.put("attempt",attempt);
        jsonMap.put("submission_url",submission_url);
        try {
            jsonString = objectMapper.writeValueAsString(jsonMap);
        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }
        return jsonString;
    }
}
