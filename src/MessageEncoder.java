
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageEncoder implements Encoder.Text<Message> {

	@Override
	public String encode(Message message) throws EncodeException {
		
//		JsonObject jsonObject = Json.createObjectBuilder().add("id", message.getSession_id())
//				.add("msg", message.getMsg()).build();
		JSONObject object=new JSONObject();
		try {
			//object.put("id", message.getSession_id());
			object.put("msg", message.getMsg());
			object.put("sender_id", message.getSender_id());
			object.put("receiver_id", message.getReciver_id());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println("jsonObject : "+object.toString());
		return object.toString();

	}

	@Override
	public void init(EndpointConfig ec) {
		System.out.println("\nMessageEncoder - init method called");
	}

	@Override
	public void destroy() {
		System.out.println("\nMessageEncoder - destroy method called");
	}

}
