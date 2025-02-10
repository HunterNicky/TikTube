package com.aps.tiktube.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import static com.aps.tiktube.security.TokenManager.*;

@Service
public class VideoService {

    private static final String VIDEO_NOT_FOUND = "Video not found";
    private static final String VIDEO_ID = "video_id";
    private static final String VIDEO = "Video";

    /**
     * Upload a video
     * 
     * @return Result
     */
    public String uploadVideo(MultipartFile file, String token, String title, String description) {
        User user = getUser(token);
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

        JSONObject response = new JSONObject();
        response.put(VIDEO_ID, videoId);
        response.put("id", video.getId());

        return response.toString();
    }

    /**
     * Add a thumbnail to a video
     * 
     * @return Result
     */
    public String addThumbnail(MultipartFile file, String token, String videoId) {
        User user = getUser(token);
        UserFileService userFileService = new UserFileService(user);

        Access<Video> videoAccess = new Access<>(Video.class);

        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEO_NOT_FOUND;
        }

        video.setThumbnailId(userFileService.getFileId(file, video, "Thumbnail"));
        video.save();

        return "Thumbnail added";
    }

    /**
     * Add a comment to a video
     * 
     * @return Result
     */
    public String addComment(String token, String videoId, String comment) {
        User user = getUser(token);

        Access<Video> videoAccess = new Access<>(Video.class);

        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEO_NOT_FOUND;
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
     * @return Result
     */
    public String addViews(String videoId, String token) {
        User user = getUser(token);
        Access<Video> videoAccess = new Access<>(Video.class);

        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEO_NOT_FOUND;
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
     * @return Result
     */
    public String likeVideo(String videoId, String token) {
        User user = getUser(token);
        Access<Video> videoAccess = new Access<>(Video.class);

        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEO_NOT_FOUND;
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
     * @return Result
     */
    public String deleteVideo(String videoId) {
        Access<Video> videoAccess = new Access<>(Video.class);
        Access<Comments> commentsAccess = new Access<>(Comments.class);
        Access<Like> likeAccess = new Access<>(Like.class);
        Access<Views> viewsAccess = new Access<>(Views.class);

        Video video = videoAccess.getById(videoId);
        List<Comments> comments = commentsAccess.where(List.of(VIDEO_ID), Collections.singletonList(videoId));
        List<Like> likes = likeAccess.where(List.of(VIDEO_ID), Collections.singletonList(videoId));
        List<Views> views = viewsAccess.where(List.of(VIDEO_ID), Collections.singletonList(videoId));

        videoAccess.close();
        commentsAccess.close();
        likeAccess.close();
        viewsAccess.close();

        if (video == null) {
            return VIDEO_NOT_FOUND;
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
     * @return Number of views
     */
    public String numOfViews(String videoId) {
        Access<Views> viewsAccess = new Access<>(Views.class);
        List<Views> views = viewsAccess.where(VIDEO_ID, videoId);
        viewsAccess.close();
        return Integer.toString(views.size());
    }

    /**
     * Get the number of likes of a video
     * 
     * @return Number of likes
     */
    public String numOfLikes(String videoId) {
        Access<Like> likeAccess = new Access<>(Like.class);
        List<Like> likes = likeAccess.where(VIDEO_ID, videoId);
        likeAccess.close();
        return Integer.toString(likes.size());
    }

    /**
     * Change the description of a video
     * 
     * @return Result
     */
    public String changeDescription(String videoId, String description) {
        Access<Video> videoAccess = new Access<>(Video.class);
        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEO_NOT_FOUND;
        }

        video.setDescripton(description);
        video.save();

        return "Description changed";
    }

    /**
     * Change the title of a video
     * 
     * @return Result
     */
    public String changeTitle(String videoId, String title) {
        Access<Video> videoAccess = new Access<>(Video.class);
        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (video == null) {
            return VIDEO_NOT_FOUND;
        }

        video.setVideoName(title);
        video.save();

        return "Title changed";
    }

    /**
     * Get the information of a video
     * 
     * @return Video information
     */
    public String getVideoInfo(String videoId) {
        Access<Video> videoAccess = new Access<>(Video.class);
        Video video = videoAccess.getById(videoId);
        videoAccess.close();

        if (!video.getExist()) {
            return VIDEO_NOT_FOUND;
        }

        Document videoInfo = video.toDocument();
        videoInfo.put("views", numOfViews(videoId));
        videoInfo.put("likes", numOfLikes(videoId));
        videoInfo.put("username", getUsername(video.getUserId()));
        JSONObject videoJson = new JSONObject(videoInfo);

        return videoJson.toString();
    }

    /**
     * Get the comments of a video
     * 
     * @return Comments
     */
    public String getComments(String videoId) {
        Access<Comments> commentsAccess = new Access<>(Comments.class);
        List<Comments> comments = commentsAccess.where(VIDEO_ID, videoId);
        commentsAccess.close();

        JSONArray commentsInfo = new JSONArray();

        for (Comments comment : comments) {
            Document commentInfo = comment.toDocument();
            commentInfo.put("UserName", getUsername(comment.getUserId()));
            commentInfo.put("id", comment.getId());
            commentsInfo.put(commentInfo);
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
        List<Video> videos = videoAccess.getCollectionAsListEnty(VIDEO);
        videoAccess.close();

        JSONArray videosInfo = new JSONArray();

        for (Video video : videos) {
            Document videoDocument = video.toDocument();
            videoDocument.put("views", numOfViews(video.getId()));
            videoDocument.put("likes", numOfLikes(video.getId()));
            videoDocument.put("username", getUsername(video.getId()));
            videoDocument.put("id", video.getId());
            videosInfo.put(videoDocument);
        }

        return videosInfo.toString();
    }

    /**
     * Sort the documents by a label
     * 
     * @return Sorted documents
     */
    private static List<Document> sortDocumentByLabel(List<Document> documents, String label) {
        Comparator<Document> comparator = (d1, d2) -> {
            Object valor1 = d1.get(label);
            Object valor2 = d2.get(label);

            if (valor1 instanceof Integer && valor2 instanceof Integer) {
                return Integer.compare((Integer) valor1, (Integer) valor2);
            } else if (valor1 instanceof Date && valor2 instanceof Date) {
                return ((Date) valor1).compareTo((Date) valor2);
            } else if (valor1 instanceof String && valor2 instanceof String) {
                return ((String) valor1).compareTo((String) valor2);
            } else {
                throw new UnsupportedOperationException("Error" + valor1.getClass());
            }
        };

        documents.sort(comparator);

        return documents;
    }

    /**
     * Get all the trending videos
     * 
     * @return Trending videos
     */
    public String getAllTrendingVideos() {
        Access<Video> videoAccess = new Access<>(Video.class);
        List<Video> videos = videoAccess.getCollectionAsListEnty(VIDEO);
        List<Document> documentsList = new ArrayList<>();

        for (Video video : videos) {
            Document videoDocument = video.toDocument();
            videoDocument.put("views", numOfViews(video.getId()));
            videoDocument.put("likes", numOfLikes(video.getId()));
            videoDocument.put("id", video.getId());
            videoDocument.put("username", getUsername(video.getUserId()));
            documentsList.add(videoDocument);
        }

        List<Document> orderDocuments = sortDocumentByLabel(documentsList, "views");
        videoAccess.close();

        JSONArray videosInfo = new JSONArray();

        orderDocuments.forEach(videosInfo::put);

        return videosInfo.toString();
    }

    /**
     * Get all the new videos
     * 
     * @return Videos
     */
    public String getAllNewVideos() {
        Access<Video> videoAccess = new Access<>(Video.class);
        List<Video> videos = videoAccess.getCollectionAsListEnty(VIDEO);
        List<Document> documentsList = new ArrayList<>();

        for (Video video : videos) {
            Document videoDocument = video.toDocument();
            videoDocument.put("id", video.getId());
            videoDocument.put("views", numOfViews(video.getId()));
            videoDocument.put("likes", numOfLikes(video.getId()));
            videoDocument.put("username", getUsername(video.getUserId()));
            documentsList.add(videoDocument);
        }

        List<Document> orderDocuments = sortDocumentByLabel(documentsList, "publish_date");
        videoAccess.close();

        JSONArray videosInfo = new JSONArray();

        orderDocuments.forEach(videosInfo::put);

        return videosInfo.toString();
    }

    /**
     * Get the videos of a user
     * 
     * @return Videos
     */
    public String getUserVideos(String userId) {
        Access<User> userAccess = new Access<>(User.class);
        User user = userAccess.getById(userId);
        userAccess.close();

        Access<Video> videoAccess = new Access<>(Video.class);
        List<Video> videos = videoAccess.where("user_id", user.getId());
        videoAccess.close();

        JSONArray videosInfo = new JSONArray();

        for (Video video : videos) {
            Document videoInfo = video.toDocument();
            videoInfo.put("views", numOfViews(video.getId()));
            videoInfo.put("likes", numOfLikes(video.getId()));
            videoInfo.put("id", video.getId());
            videoInfo.put("username", getUsername(video.getUserId()));
            videosInfo.put(videoInfo);
        }

        return videosInfo.toString();
    }

    /**
     * Get the videos of a user liked
     * 
     * @return Videos liked
     */
    public String getUserLikes(String token) {
        User user = getUser(token);
        Access<Like> likeAccess = new Access<>(Like.class);
        List<Like> likes = likeAccess.where("user_id", user.getId());
        likeAccess.close();

        JSONArray likesInfo = new JSONArray();

        Access<Video> videoAccess = new Access<>(Video.class);
        for (Like like : likes) {
            Document likeInfo = like.toDocument();
            Video video = videoAccess.getById(like.getVideoId());
            Document videoDocument = video.toDocument();
            videoDocument.put("views", numOfViews(video.getId()));
            videoDocument.put("likes", numOfLikes(video.getId()));
            videoDocument.put("username", getUsername(video.getUserId()));
            likeInfo.put("video", videoDocument);
            likesInfo.put(likeInfo);
        }
        videoAccess.close();

        return likesInfo.toString();
    }

    private String getUsername(String userId) {
        Access<User> userAccess = new Access<>(User.class);
        User user = userAccess.getById(userId);
        userAccess.close();
        return user.getUserName();
    }
}