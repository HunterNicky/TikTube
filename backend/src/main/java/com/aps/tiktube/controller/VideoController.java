package com.aps.tiktube.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aps.tiktube.model.Access;
import com.aps.tiktube.model.video.Video;
import com.aps.tiktube.security.TokenManager;
import com.aps.tiktube.service.VideoService;

import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class VideoController {
    private static final String INVALIDTOKEN = "Invalid token";
    private static final String UNREGISTERED = "unregistered";
    private static final String UNAUTHORIZED = "Unauthorized";
    private static final String VIDEO = "Video";

    @PostMapping("/uploadvideo")
    public ResponseEntity<Object> uploadVideo(@RequestParam("file") MultipartFile file,
            @RequestParam("token") String token, @RequestParam("title") String title,
            @RequestParam("description") String description) {
        if (!TokenManager.verify(token))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        if (file.isEmpty())
            return ResponseEntity.badRequest().body("File is empty");

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.uploadVideo(file, token, title, description));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading video");
        }
    }

    @PostMapping("/addthumbnail")
    public ResponseEntity<Object> addThumbnail(@RequestParam("file") MultipartFile file,
            @RequestParam("token") String token,
            @RequestParam("videoId") String videoId){
        if (!TokenManager.verify(token))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        if (Boolean.FALSE.equals(TokenManager.verifyTokenAccess(token, videoId, VIDEO))) {
            return ResponseEntity.status(401).body(UNAUTHORIZED);
        }

        if (file.isEmpty())
            return ResponseEntity.badRequest().body("File is empty");

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.addThumbnail(file, token, videoId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding thumbnail");
        }
    }

    @DeleteMapping("/deletevideo")
    public ResponseEntity<Object> deleteVideo(@RequestParam("token") String token,
            @RequestParam("videoId") String videoId) {
        if (!TokenManager.verify(token))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        if (Boolean.FALSE.equals(TokenManager.verifyTokenAccess(token, TokenManager.getUser(token).getId(), VIDEO))) {
            return ResponseEntity.status(401).body(UNAUTHORIZED);
        }

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.deleteVideo(videoId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting video");
        }
    }

    @PostMapping("/likevideo")
    public ResponseEntity<Object> likeVideo(@RequestParam("token") String token,
            @RequestParam("videoId") String videoId) {
        if (!TokenManager.verify(token))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.likeVideo(token, videoId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error liking video");
        }
    }

    @PostMapping("/commentvideo")
    public ResponseEntity<Object> commentVideo(@RequestParam("token") String token,
            @RequestParam("videoId") String videoId,
            @RequestParam("comment") String comment) {
        if (!TokenManager.verify(token))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.addComment(token, videoId, comment));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error commenting video");
        }
    }

    @GetMapping("/getmedia/{videoId}/{token}/{bucketName}")
    public ResponseEntity<Object> getVideo(@PathVariable("videoId") String videoId,
            @PathVariable("token") String token,
            @PathVariable("bucketName") String bucketName) {
        if (!TokenManager.verify(token) && !token.equals(UNREGISTERED))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            Access<Video> videoAccess = new Access<>(Video.class);
            Object object = videoAccess.getMediaResource(videoId, bucketName);
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("video/mp4"))
                    .body(object == null ? "Error getting video" : object);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting video");
        }
    }

    @GetMapping("/getvideoinfo/{videoId}/{token}")
    public ResponseEntity<Object> getVideoInfo(@PathVariable("videoId") String videoId,
            @PathVariable("token") String token) {
        if (!TokenManager.verify(token) && !token.equals(UNREGISTERED))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.getVideoInfo(videoId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting video info");
        }
    }

    @GetMapping("/getallvideos/{token}")
    public ResponseEntity<Object> getAllVideos(@PathVariable("token") String token) {
        try {
            if (!TokenManager.verify(token) && !token.equals(UNREGISTERED))
                return ResponseEntity.status(401).body(INVALIDTOKEN);

            TokenManager.updateTokenLastTimeUsed(token);

            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.getAllVideos());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting videos");
        }
    }

    @GetMapping("/getalltrendingvideos/{token}")
    public ResponseEntity<Object> getAllTrendingVideos(@PathVariable("token") String token) {
        if (!TokenManager.verify(token) && !token.equals(UNREGISTERED))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.getAllTrendingVideos());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting trending videos");
        }
    }

    @GetMapping("/getallvideosbyuser/{userId}/{token}")
    public ResponseEntity<Object> getAllVideosByUser(@PathVariable("userId") String userId,
            @PathVariable("token") String token) {
        if (!TokenManager.verify(token) && !token.equals(UNREGISTERED))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.getUserVideos(userId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting videos by user");
        }
    }

    @GetMapping("/getallnewvideos/{token}")
    public ResponseEntity<Object> getAllNewVideos(@PathVariable("token") String token) {
        if (!TokenManager.verify(token) && !token.equals(UNREGISTERED))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.getAllNewVideos());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting new videos");
        }
    }

    @GetMapping("/getcomments/{videoId}/{token}")
    public ResponseEntity<Object> getComments(@PathVariable("videoId") String videoId,
            @PathVariable("token") String token) {
        if (!TokenManager.verify(token) && !token.equals(UNREGISTERED))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.getComments(videoId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting comments");
        }
    }

    @GetMapping("/getuserlikes/{videoId}/{token}")
    public ResponseEntity<Object> getUserLikes(@PathVariable("videoId") String videoId,
            @PathVariable("token") String token) {
        if (!TokenManager.verify(token))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.getUserLikes(videoId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting user likes");
        }
    }

    @Update("/changedescription")
    public ResponseEntity<Object> changeDescription(@RequestParam("token") String token,
            @RequestParam("videoId") String videoId, @RequestParam("description") String description) {
        if (!TokenManager.verify(token))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        if (Boolean.FALSE.equals(TokenManager.verifyTokenAccess(token, TokenManager.getUser(token).getId(), VIDEO))) {
            return ResponseEntity.status(401).body(UNAUTHORIZED);
        }

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.changeDescription(videoId, description));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error changing description");
        }
    }

    @Update("/changetitle")
    public ResponseEntity<Object> changeTitle(@RequestParam("token") String token,
            @RequestParam("videoId") String videoId,
            @RequestParam("title") String title) {
        if (!TokenManager.verify(token))
            return ResponseEntity.status(401).body(INVALIDTOKEN);

        if (Boolean.FALSE.equals(TokenManager.verifyTokenAccess(token, TokenManager.getUser(token).getId(), VIDEO))) {
            return ResponseEntity.status(401).body(UNAUTHORIZED);
        }

        TokenManager.updateTokenLastTimeUsed(token);

        try {
            VideoService videoService = new VideoService();
            return ResponseEntity.ok(videoService.changeTitle(videoId, title));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error changing title");
        }
    }

}
