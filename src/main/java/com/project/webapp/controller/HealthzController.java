package com.project.webapp.controller;

import com.project.webapp.service.DatabaseService;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthzController {
    @Autowired
    DatabaseService databaseService;
    @Value("${availabilityZone}")
    String availabilityZone;
    private static final Logger logger = LogManager.getLogger(HealthzController.class);
    private static final StatsDClient statsd = new NonBlockingStatsDClient("healthz", "localhost", 8125);

    @GetMapping("/healthz")
    public ResponseEntity<String> getHealth(@RequestBody(required = false) String requestBody, HttpServletRequest request) {
        statsd.incrementCounter("getHealthzCounter");
        logger.debug("Healthz Endpoint hit");
        if (requestBody != null || !request.getParameterMap().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("BadRequest", "Invalid Request").cacheControl(CacheControl.noCache()).build();
        } else if (databaseService.isServerRunning()) {
            return ResponseEntity.ok().header("OK", "Success").cacheControl(CacheControl.noCache()).build();
        } else {
            logger.error("Service Unavailable: Database is down");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).header("Unavailable", "The Server Cannot Handle The Request").cacheControl(CacheControl.noCache()).build();
        }
    }

    @GetMapping("/")
    public String getMetadata(){
        return availabilityZone;
    }

    //any methods other than GET for the same endpoint
    @RequestMapping(value = "/healthz", method = {RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.HEAD, RequestMethod.OPTIONS})
    public ResponseEntity<String> anyOtherMethod() {
        logger.warn("Method not allowed at /healthz");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header("NotAllowed", "The Requested Method Is Not Allowed").cacheControl(CacheControl.noCache()).build();
    }

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.HEAD, RequestMethod.OPTIONS})
    public ResponseEntity<String> anyOtherUrl() {
        logger.warn("URL Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header("NotFound", "The Requested Resource Is Not Found").cacheControl(CacheControl.noCache()).build();
    }
}
