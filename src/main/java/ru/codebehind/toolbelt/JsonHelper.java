package ru.codebehind.toolbelt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static <T> T Deserialize(Class<T> clazz, File src) throws Exception {
		return objectMapper.readValue(src, clazz);
	}

	public static <T> T Deserialize(Class<T> clazz, String src) throws Exception {
		return objectMapper.readValue(src, clazz);
	}
	
	public static <T> T Deserialize(Class<T> clazz, DataInput src) throws Exception {
		return objectMapper.readValue(src, clazz);
	}
	
	public static void Serialize(DataOutput out, Object value) throws Exception {
		objectMapper.writeValue(out, value);
	} 
}
