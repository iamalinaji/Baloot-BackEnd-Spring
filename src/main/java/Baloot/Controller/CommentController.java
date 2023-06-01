package Baloot.Controller;


import Baloot.Service.MarketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;




@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    private final MarketService marketService;

    public CommentController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getComments() {
        try {
            return ResponseEntity.ok(marketService.getCommentList());
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<?> getCommmentById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(marketService.getCommentById(id));
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody Map<String, String> request) {
        int commodityId = Integer.parseInt(request.get("commodityId"));
        String comment = request.get("comment");
        String loggedInUser = marketService.getLoggedInUser();
        try {
            marketService.addComment(loggedInUser, commodityId, comment);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Comment added successfully");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @PostMapping("/comments/like")
    public ResponseEntity<?> likeComment(@RequestBody Map<String, String> request) {
        int id = Integer.parseInt(request.get("id"));
        String loggedInUser = marketService.getLoggedInUser();
        try {
            marketService.vote(loggedInUser,1, id);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Comment liked successfully");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @PostMapping("/comments/dislike")
    public ResponseEntity<?> dislikeComment(@RequestBody Map<String, String> request) {
        int id = Integer.parseInt(request.get("id"));
        String loggedInUser = marketService.getLoggedInUser();
        try {
            marketService.vote(loggedInUser,-1, id);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Comment disliked successfully");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

}
