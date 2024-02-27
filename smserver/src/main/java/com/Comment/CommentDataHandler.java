package com.Comment;

import com.DbSequencer.IdSequencer;
import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Post.PostRepository;
import com.Topic.Topic;
import com.Topic.TopicDataHandler;
import com.Topic.TopicRepository;
import com.User.User;
import com.User.UserDataHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
// This class is used to service incoming requests related to Comment data.
// It uses repositories to get or set data and sends the result back to the server code
public class CommentDataHandler {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private PostDataHandler postHandler;
    private TopicDataHandler topicHandler;
    private UserDataHandler userHandler;
    private IdSequencer idSequencer;

    public Comment insert(Comment comment) throws DBEntryAlreadyExistsException, NoSuchDBEntryException {
        if (comment.getId() < IdSequencer.START_ID_VALUE) {
            int newID = idSequencer.getNextId(IdSequencer.Collection.COMMENT);
            IdSequencer.lastCommentId = newID;
            comment.setId(newID);
        }

        if (commentRepository.getById(comment.getId()).isPresent()) {
            System.out.println("FAILED insert comment to database.  Comment already exists.     " + comment);
            throw new DBEntryAlreadyExistsException("Comment with id: " + comment.getId() + " already exists");
        }
        Optional<Post> postToBeAddedToOptional = postRepository.getById(comment.getPostId());
        if (postToBeAddedToOptional.isEmpty()) {
            System.out.println("FAILED insert comment to database.  Comment's Post does not exist.");
            throw new NoSuchDBEntryException("No Post found with name: " + comment.getPostId()); // No such post
        }
        Post postToBeAddedTo = postToBeAddedToOptional.get();

        if (postToBeAddedTo.contains(comment)) {
            System.out.println("FAILED insert comment to database.  Comment already added to post.     " + comment);
            throw new DBEntryAlreadyExistsException("Comment with id: " + comment.getId() + " already exists in post: " + postToBeAddedTo);
        }
        User creatorUser = userHandler.getByName(comment.getCreatorName()); // throws NoSuchDBEntryException if user doesn't exist
        if (!creatorUser.getProfile().commentIdsContains(comment.getId()))
            creatorUser.getProfile().addCommentId(comment.getId());
        userHandler.save(creatorUser);

        postToBeAddedTo.addComment(comment);
        postHandler.save(postToBeAddedTo);

        Topic topicToBeAddedTo = topicHandler.getByName(postToBeAddedTo.getTopicName());
        topicToBeAddedTo.updateTopicPostWithID(postToBeAddedTo);
        topicHandler.save(topicToBeAddedTo);

        return commentRepository.insert(comment);
    }

    // Comment needs the id and the postId
    public void remove(Comment comment) throws NoSuchDBEntryException {
        Optional<Post> postOptional = postRepository.getById(comment.getPostId());

        if (postOptional.isEmpty()) {
            throw new NoSuchDBEntryException("No Post found with id: " + comment.getPostId());
        }
        Post post = postOptional.get();

        User creatorUser = userHandler.getByName(comment.getCreatorName()); // throws NoSuchDBEntryException if user doesn't exist
        //if (creatorUser.getProfile().commentIdsContains(comment.getId())) {
            creatorUser.getProfile().removeCommentId(comment.getId());
            System.out.println("user: " + creatorUser.getUsername() + "   removeCommentId(" + comment.getId() + ")");
        //}
        //else
            //System.out.println("user: " + creatorUser.getUsername() + "   doesn't contain commentId: " + comment.getId());
        userHandler.save(creatorUser);

        post.removeComment(comment.getId());
        postHandler.save(post);

        Topic topicToBeAddedTo = topicHandler.getByName(post.getTopicName());
        topicToBeAddedTo.updateTopicPostWithID(post);
        topicHandler.save(topicToBeAddedTo);

        commentRepository.deleteById(comment.getId());
    }

    public ArrayList<Comment> getAllCommentsOnPost(Post post) throws NoSuchDBEntryException {
        Optional<ArrayList<Comment>> commentsOptional = commentRepository.getByPostId(post.getId());

        if (commentsOptional.isEmpty()) { // No post found in database with that Id
            throw new NoSuchDBEntryException("No Post found with id: " + post.getId());
        }

        return commentsOptional.get();
    }

    public void removePost(Post post) throws NoSuchDBEntryException {
        // fully removes all comments from the post
        ArrayList<Comment> allComments = getAllCommentsOnPost(post);
        for (Comment curComment : allComments) {
            try {
                remove(curComment);
            } catch (NoSuchDBEntryException e) {
                System.out.println("comment: " + curComment + " on Post: " + post + " is not in database");
                e.printStackTrace();
                continue;
            }
        }

        postHandler.remove_incomplete(post.getId());
    }

    public boolean exists(int id){
        return commentRepository.getById(id).isPresent();
    }

    public Comment getById(int id) throws NoSuchDBEntryException {
        Optional<Comment> foundCommentOptional = commentRepository.getById(id);

        if (foundCommentOptional.isEmpty())
            throw new NoSuchDBEntryException("No Comment found with id: " + id);  // exception if no Comment found

        return foundCommentOptional.get();
    }

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public void deleteAll() {
        commentRepository.deleteAll();
    }
}
