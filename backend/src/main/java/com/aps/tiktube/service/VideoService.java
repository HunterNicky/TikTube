package com.aps.tiktube.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aps.tiktube.model.Access;
import com.aps.tiktube.model.User;
import com.aps.tiktube.model.video.Comments;
import com.aps.tiktube.model.video.Like;
import com.aps.tiktube.model.video.Video;
import com.aps.tiktube.model.video.Views;
import com.aps.tiktube.security.TokenManager;

@Service
public class VideoService {

    private static final String VIDEONOTFOUND = "Video not found";
    private static final String VIDEOID = "VideoId";
    private static final String VIDEO = "Video";

    /**
     * Upload a video
     * 
     * @param file
     * @param token
     * @param title
     * @param description
     * @return Result
     */
    public String uploadVideo(MultipartFile file, String token, String title, String description) {
        User user = TokenManager.getUser(token);
        UserFileService userFileService = new UserFileService(user);

        Video video = new Video();

        final String videoId = userFileService.getFileId(file, video, VIDEO);

        video.setVideoId(videoId);
        video.setDescripton(description);
        video.setVideoName(title);
        video.setUserId(user.getId());
        video.setPublishDate(new Date(System.currentTimeMillis()));
        video.setThumbnailId(null);
        video.save();

        return "Video uploaded";
    }

    /**
     * Add a thumbnail to a video
     * 
     * @param file
     * @param token
     * @param videoId
     * @return Result
     */
    public String addThumbnail(MultipartFile file, String token, String videoId) {
        User user = TokenManager.getUser(token);
        UserFileService userFileService = new UserFileService(user);

        Access<Video> videoAccess = new Access<>(Video.class);

        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEONOTFOUND;
        }

        video.setThumbnailId(userFileService.getFileId(file, video, "Thumbnail"));
        video.save();

        return "Thumbnail added";
    }

    /**
     * Add a comment to a video
     * 
     * @param token
     * @param videoId
     * @param comment
     * @return Result
     */
    public String addComment(String token, String videoId, String comment) {
        User user = TokenManager.getUser(token);

        Access<Video> videoAccess = new Access<>(Video.class);

        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEONOTFOUND;
        }

        Comments comments = new Comments();

        comments.setData(new Date(System.currentTimeMillis()));
        comments.setUserId(user.getId());
        comments.setComment(comment);
        comments.setVideoId(videoId);
        comments.save();

        return "Comment added";
    }

    /**
     * Add views to a video
     * 
     * @param videoId
     * @param token
     * @return Result
     */
    public String addViews(String videoId, String token) {
        User user = TokenManager.getUser(token);
        Access<Video> videoAccess = new Access<>(Video.class);

        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEONOTFOUND;
        }

        Views views = new Views();

        views.setUserId(user.getId());
        views.setVideoId(videoId);
        views.save();

        return "Views added";
    }

    /**
     * Like a video
     * 
     * @param videoId
     * @param token
     * @return Result
     */
    public String likeVideo(String videoId, String token) {
        User user = TokenManager.getUser(token);
        Access<Video> videoAccess = new Access<>(Video.class);

        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEONOTFOUND;
        }

        Like like = new Like();

        like.setUserId(user.getId());
        like.setVideoId(videoId);
        like.save();

        return "Video liked";
    }

    /**
     * Delete a video
     * 
     * @param videoId
     * @return Result
     */
    public String deleteVideo(String videoId) {
        Access<Video> videoAccess = new Access<>(Video.class);
        Access<Comments> commentsAccess = new Access<>(Comments.class);
        Access<Like> likeAccess = new Access<>(Like.class);
        Access<Views> viewsAccess = new Access<>(Views.class);

        Video video = videoAccess.getById(videoId);
        List<Comments> comments = commentsAccess.where(Arrays.asList(VIDEOID), Arrays.asList(videoId));
        List<Like> likes = likeAccess.where(Arrays.asList(VIDEOID), Arrays.asList(videoId));
        List<Views> views = viewsAccess.where(Arrays.asList(VIDEOID), Arrays.asList(videoId));

        videoAccess.close();
        commentsAccess.close();
        likeAccess.close();
        viewsAccess.close();

        if (video == null) {
            return VIDEONOTFOUND;
        }

        for (Comments comment : comments) {
            commentsAccess.deleteById(comment.getId());
        }
        for (Like like : likes) {
            likeAccess.deleteById(like.getId());
        }
        for (Views view : views) {
            viewsAccess.deleteById(view.getId());
        }
        videoAccess.deleteById(videoId);

        return "Video deleted";
    }

    /**
     * Get the number of views of a video
     * 
     * @param videoId
     * @return Number of views
     */
    public String numOfViews(String videoId) {
        Access<Views> viewsAccess = new Access<>(Views.class);
        List<Views> views = viewsAccess.where(Arrays.asList(VIDEOID), Arrays.asList(videoId));
        viewsAccess.close();
        return Integer.toString(views.size());
    }

    /**
     * Get the number of likes of a video
     * 
     * @param videoId
     * @return Number of likes
     */
    public String numOfLikes(String videoId) {
        Access<Like> likeAccess = new Access<>(Like.class);
        List<Like> likes = likeAccess.where(Arrays.asList(VIDEOID), Arrays.asList(videoId));
        likeAccess.close();
        return Integer.toString(likes.size());
    }

    /**
     * Get the number of comments of a video
     * 
     * @param videoId
     * @return Number of comments
     */
    public String numOfComments(String videoId) {
        Access<Comments> commentsAccess = new Access<>(Comments.class);
        List<Comments> comments = commentsAccess.where(Arrays.asList(VIDEOID), Arrays.asList(videoId));
        commentsAccess.close();
        return Integer.toString(comments.size());
    }

    /**
     * Change the description of a video
     * 
     * @param videoId
     * @param description
     * @return Result
     */
    public String changeDescription(String videoId, String description) {
        Access<Video> videoAccess = new Access<>(Video.class);
        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEONOTFOUND;
        }

        video.setDescripton(description);
        video.save();

        return "Description changed";
    }

    /**
     * Change the title of a video
     * 
     * @param videoId
     * @param title
     * @return Result
     */
    public String changeTitle(String videoId, String title) {
        Access<Video> videoAccess = new Access<>(Video.class);
        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEONOTFOUND;
        }

        video.setVideoName(title);
        video.save();

        return "Title changed";
    }

    /**
     * Get the information of a video
     * 
     * @param videoId
     * @return Video information
     */
    public String getVideoInfo(String videoId) {
        Access<Video> videoAccess = new Access<>(Video.class);
        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEONOTFOUND;
        }

        JSONObject videoInfo = new JSONObject();

        videoInfo.put("Title", video.getVideoName());
        videoInfo.put("Description", video.getDescripton());
        videoInfo.put("Publish Date", video.getPublishDate());
        videoInfo.put("Number of Views", numOfViews(videoId));
        videoInfo.put("Number of Likes", numOfLikes(videoId));

        return videoInfo.toString();
    }

    /**
     * Get the comments of a video
     * 
     * @param videoId
     * @return Comments
     */
    public String getComments(String videoId) {
        Access<Comments> commentsAccess = new Access<>(Comments.class);
        List<Comments> comments = commentsAccess.where(Arrays.asList(VIDEOID), Arrays.asList(videoId));
        commentsAccess.close();

        JSONObject commentsInfo = new JSONObject();

        for (Comments comment : comments) {
            JSONObject commentInfo = new JSONObject();
            commentInfo.put("User", comment.getUserId());
            commentInfo.put("Comment", comment.getComment());
            commentInfo.put("Date", comment.getData());
            commentsInfo.put(comment.getId(), commentInfo);
        }

        return commentsInfo.toString();
    }

    /**
     * Get all the videos
     * 
     * @return Videos
     */     
    public String getAllVideos() {
        Access<Video> videoAccess = new Access<>(Video.class);
        List<Document> videos = videoAccess.getCollectionAsList(VIDEO);
        videoAccess.close();

        JSONArray videosInfo = new JSONArray();

        for (Document video : videos) {
            JSONObject videoInfo = new JSONObject();
            videoInfo.put("id", video.get(VIDEOID).toString());
            videoInfo.put("title", video.get("VideoName"));
            videoInfo.put("publish_date", video.get("PublishDate"));
            videoInfo.put("views", numOfViews(video.get(VIDEOID).toString()));
            videosInfo.put(videoInfo);
        }

        return videosInfo.toString();
    }

    /**
     * Get the videos of a user liked
     * 
     * @param token
     * @return Videos liked
     */
    public String getUserLikes(String token) {
        User user = TokenManager.getUser(token);
        Access<Like> likeAccess = new Access<>(Like.class);
        List<Like> likes = likeAccess.where(Arrays.asList("userId"), Arrays.asList(user.getId()));
        likeAccess.close();

        JSONObject likesInfo = new JSONObject();

        for (Like like : likes) {
            JSONObject likeInfo = new JSONObject();
            likeInfo.put(VIDEO, like.getVideoId());
            likesInfo.put(like.getId(), likeInfo);
        }

        return likesInfo.toString();
    }
}
