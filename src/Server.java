import java.io.IOException;
import java.util.ArrayList;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ServerEndpoint(value = "/user/{email}", encoders = { MessageEncoder.class }, decoders = { MessageDecoder.class })
public class Server {

	private static ArrayList<User_Online> online = new ArrayList<User_Online>();

	@OnOpen
	public void onOpen(Session session, @PathParam("email") String email) throws IOException {
		User_Online user = new User_Online();
		user.setEmail(email);
		System.out.println(email);
		user.setSession(session);
		user.setName(email);
		online.add(user);
		System.out.print("New User : " + session.getId());
		try {
			JSONObject json = new JSONObject();
			JSONArray array = new JSONArray();
			for (int i = 0; i < online.size(); i++) {
//				if (online.get(i).getEmail() != email) {
					JSONObject obj = new JSONObject();
					obj.put("id", online.get(i).getSession().getId().toString());
					obj.put("name", online.get(i).getName().toString());
					obj.put("email", online.get(i).getEmail().toString());

					array.put(obj);
//				}
			}
			json.put("online_user", array);
			json.put("isMessage", Integer.toString(0));
			for (int i = 0; i < online.size(); i++) {
				online.get(i).getSession().getBasicRemote().sendText(json.toString());
			
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@OnMessage
	public void onMessage(Message message, Session s) throws JSONException {
		System.out.println("onMessage :- " + message.getMsg());

		synchronized (online) {
			for (User_Online client : online) {
				if (client.getSession().getId().equals(message.getReciver_id())) {
					System.out.println("syns onMessage :- " + message.getMsg());
					JSONObject obj = new JSONObject();
					obj.put("sender_id", message.getSender_id());
					obj.put("receiver_id", message.getReciver_id());
					obj.put("msg", message.getMsg());
					obj.put("isMessage", Integer.toString(1));
					Message response = new Message();
					response.setMsg(message.getMsg());
					response.setReciver_id(message.getReciver_id());
					response.setSender_id(message.getSender_id());
					try {
//						client.getSession().getBasicRemote().sendObject(response);
						client.getSession().getBasicRemote().sendText(obj.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@OnError
	public void error(Throwable t) {
	}

	@OnClose
	public void closedConnection(Session session) {
		for (User_Online user : online) {
			if (user.getSession() == session) {
				online.remove(user);
			}
		}

	}

}
