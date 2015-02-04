package own.tetchi.apps.soundl.ly.sample.tools;

public class Log {
	public static void d(String tag, String msg) {
		if (UIStatic.TEST_MODE)
			android.util.Log.d(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (UIStatic.TEST_MODE)
			android.util.Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (UIStatic.TEST_MODE)
			android.util.Log.e(tag, msg);
	}

	public static void e(Exception e) {
		if (UIStatic.TEST_MODE)
			e.printStackTrace();
	}
}
