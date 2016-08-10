package ru.codebehind.toolbelt;

public class DateHelper {
	public static long GetSystemUnixTime() {
		return (long) (System.currentTimeMillis() / 1000L);
	}
}
