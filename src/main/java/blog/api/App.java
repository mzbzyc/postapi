package blog.api;

import java.util.List;

import com.blade.Blade;

import blade.kit.json.JSONKit;
import blade.kit.json.Json;
import blade.kit.json.JsonArray;
import blade.kit.json.JsonObject;
import blade.kit.log.Logger;
import blade.plugin.sql2o.Sql2oPlugin;
import blog.api.service.Service;
import blog.api.service.impl.ServiceImpl;

/**
 * 启动配置类
 * 
 * @author biezhi
 */
public class App {

	private static final Logger LOGGER = Logger.getLogger(App.class);
	
	private static final int HTTP_BAD_REQUEST = 400;
	
	private static Service service = new ServiceImpl();
	
	public static <T> String dataToJson(List<T> data) {
		String json = JSONKit.toJSONString(data);
		return json;
	}
	
	public static void main(String[] args) throws Exception {
		Blade blade = Blade.me();
		
		// 配置路由
		blade.get("/posts", (request, response) -> {
			response.status(200);
			response.json(dataToJson(service.getAllPosts()));
		});
		
		// 保存
		blade.post("/post/save", (request, response) ->{
			LOGGER.info(" request body = " + request.body());
			
			JsonObject creation = Json.parse(request.body().asString()).asObject();
			
			if (null == creation) {
				response.status(HTTP_BAD_REQUEST);
				response.text("");
				return;
			}
			
			String title = creation.getString("title");
			String content = creation.getString("content");
			JsonArray categories = creation.get("categories").asArray();
			
			String id = service.createPost(title, content, categories.values());
			response.status(200);
			response.text(id);
		});
		
		// 添加一条评论
		blade.post("/posts/:post_id/comments", (request, response) ->{
			LOGGER.info(" request body = " + request.body());
			
			JsonObject creation = Json.parse(request.body()).asObject();
			
//			NewCommentPayload creation = JSONKit.parse(request.body(), NewCommentPayload.class);
			if (null == creation) {
				response.status(HTTP_BAD_REQUEST);
				response.json("");
				return;
			}
			
			String post = request.param("post_id");
			
			if (!service.existPost(post)) {
				response.status(400);
				response.json("");
				return;
			}
			
			String author = creation.getString("author");
			String content = creation.getString("content");
			
			String id = service.createComment(post, author, content);
			response.status(200);
			response.json(id);
			
		});
		
		blade.get("/posts/:post_id/comments", (request, response) -> {
			String post = request.param("post_id");
			if (!service.existPost(post)) {
				response.status(400);
				response.json("");
				return;
			}
			response.status(200);
			response.json(dataToJson(service.getAllCommentsOn(post)));
		});
		
		// 配置数据库
		Sql2oPlugin sql2oPlugin = blade.plugin(Sql2oPlugin.class);
		sql2oPlugin.config("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/blog_api", "root", "root");
		sql2oPlugin.run();
		
		Blade.me().start();
	}

}
