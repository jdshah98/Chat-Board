import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageDecoder implements Decoder.Text<Message> {

	@Override
	public Message decode(String jsonMessage) throws DecodeException{
		System.out.println("Decoder : " + jsonMessage);
		JSONObject jsonObject = null;
		try {
//			System.out.println("1Decoder : " + jsonMessage);
			jsonObject = new JSONObject(new String(jsonMessage));
//			System.out.println("2Decoder : " + jsonMessage);
//			System.out.println(jsonObject + "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Message message = new Message();
		try {
			message.setReciver_id(jsonObject.getString("receiver_id"));
			message.setSender_id(jsonObject.getString("sender_id"));
			message.setMsg(jsonObject.getString("msg"));
			System.out.println("msg get in decoder : "+message.getMsg());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println("Decoder : " + message.getMsg());
		return message;
//		System.out.println("Decoder : " + jsonMessage);

	}

	@Override
	public void init(EndpointConfig ec) {
		System.out.println("\nMessageDecoder -init method called");

	}

	@Override
	public void destroy() {
		System.out.println("\nMessageDecoder - destroy method called");
	}

	@Override
	public boolean willDecode(String arg0) {
		return true;
	}

}
