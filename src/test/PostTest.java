package test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.TreeSet;

import javax.xml.bind.ValidationException;

import com.google.gson.JsonObject;

import DAO.CommentDAO;
import DAO.PostDAO;
import model.Album;
import model.CachedObjects;
import model.Comment;
import model.Post;
import model.User;

public class PostTest {
	public static void main(String [] args) throws SQLException{
		
		System.out.println("---------------------TEST 1 ---------------------");
		System.out.println("------------------CREATE NEW POST-------------------");
		
		User u = null;
		Post p = null;
		Album a = null;
		TreeSet<String> tags = new TreeSet<>();
		tags.add("cute");
		tags.add("cat");
		
		try {
			u = new User("werewolf", "werewolfss@abv.bg", "12345", 1L);
		} catch (ValidationException e) {
			System.out.println("ops" + e.getMessage());
		}				
		try {
			a = new Album("default", "def",  LocalDate.now(), u);
			a.setAlbumId(1);
			
			p = new Post(u, "cat", "one cat", LocalDate.now(), "http://i.imgur.com/EOdEWqM.png", tags);
			
			TreeSet<String> tagsss = new TreeSet<>();
			
			tagsss.addAll(p.getTags());
			
			//Don't forget to set the ID
			p.setPostId("EOdEWqM");
			
			
			PostDAO.getInstance().makePost(u, p.getName(), p.getDescription(), p.getDateCreated(), p.getPicturePath(), tagsss, a, "EOdEWqM", "sasdafs");
			System.out.println("All albums in cached objects : " + CachedObjects.getInstance().getAllPosts().size());
			Post post4e  = CachedObjects.getInstance().getOnePost(p.getPostId(), a.getAlbumId());
			
			System.out.println("Name: " + post4e.getName());
			System.out.println("Link: " + post4e.getPicturePath());
			System.out.println("ID: " + post4e.getPostId());
		} catch (ValidationException e) {
			System.out.println("ops" + e.getMessage());
		}
		
		//WORKING 
		//add tag and post to DB and to cachedObj too		
		System.out.println("---------------------TEST 2 ---------------------");
		System.out.println("------------------CHANGE DESCRIPTION -------------------");
		
		System.out.println("Old description: " + p.getDescription());		
		
		try {
			p = PostDAO.getInstance().editPostInfo(p, u, "Cats cats cat1");
		} catch (ValidationException e) {
			System.out.println("ops!" + e.getMessage());
		}
		
		System.out.println("New description: " + p.getDescription());
		//Working
		
		System.out.println("---------------------TEST 3 ---------------------");
		System.out.println("------------------CHANGE NAME -------------------");
		
		System.out.println("Old name: " + p.getName());
		
		try {
			p = PostDAO.getInstance().editPostName(p, u, "Whaaat?");
		} catch (ValidationException e) {
			System.out.println("ops");
		}
		
		System.out.println("New name: " + p.getName());
		//Working
		
		System.out.println("---------------------TEST 4 ---------------------");
		System.out.println("------------------CHANGE TAGS -------------------");
		
		TreeSet<String> newTags = new TreeSet<>();
		newTags.add("nope");
		newTags.add("lol");
		
		System.out.println("Old tags: " + p.getTags().toString());
		
		try {
			p = PostDAO.getInstance().editTags(p, u, newTags);
		} catch (ValidationException e) {
			System.out.println("Ops!");
		}
		
		System.out.println("New tags: " + p.getTags().toString());
		//Working
		
		System.out.println("---------------------TEST 5 ---------------------");
		System.out.println("------------------DELETE POST -------------------");
		
//		System.out.println("Post exists: " + CachedObjects.getInstance().getAllPosts().get(a.getAlbumId()).containsKey(p.getPostId()));
//		
//		try {
//			PostDAO.getInstance().deletePost(p, u, a);
//		} catch (ValidationException e) {
//			System.out.println("ops!!!");
//		}	
//		
//		System.out.println("Post exists: " + CachedObjects.getInstance().getAllPosts().get(a.getAlbumId()).containsKey(p.getPostId()));
		//working
	
		System.out.println("---------------------TEST 6 ---------------------");
		System.out.println("------------------GET POST FROM DB -------------------");
				
		CachedObjects.getInstance().addUser(u);
		
		Post postchence = null;
		System.out.println("Get the post... ");
		try {
			postchence = PostDAO.getInstance().createPost("EOdEWqM", "sasdafs");
		} catch (ValidationException e) {
			System.out.println("ops");
		}
		
		try {
			Comment c = CommentDAO.getInstance().makeComment(postchence, u, null , "Sooo cute");
		} catch (ValidationException e) {
			System.err.println("ops!");
		}
		
		System.out.println("The JSON of it:");
		JsonObject post = postchence.getAsJSON();
		System.out.println(post.toString());
		//Working	
	}
}
