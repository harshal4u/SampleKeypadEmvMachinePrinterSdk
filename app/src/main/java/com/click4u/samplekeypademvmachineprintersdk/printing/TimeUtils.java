package com.click4u.samplekeypademvmachineprintersdk.printing;

public class TimeUtils {

	public static void WaitMs(long ms) {
		long time = System.currentTimeMillis();
		while (System.currentTimeMillis() - time < ms)
			;
	}
}
