import java.util.HashMap;

class Test_finally {
	private HashMap<String, String> map;

	public void onEvent(String sender, String event) {
		synchronized (map) {
			map.put(sender, event);
		}
	}

	public void pattern(Object o) {
		synchronized (o) {
			o.hashCode();
			o.hashCode();
		}
	}
}