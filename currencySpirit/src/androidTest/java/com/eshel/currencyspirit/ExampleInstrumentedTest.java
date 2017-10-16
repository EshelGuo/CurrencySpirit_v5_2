package com.eshel.currencyspirit;

import android.content.Context;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
	@Test
	public void useAppContext() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();
		JSONObject object = new JSONObject("{\"fb_level\":5,\"aaa\":\"bbb\",\"ccc\":false,\"ddd\":121.1,\"eee\":'c'}");
		Bundle bundle = new Bundle();
		Iterator<String> keys = object.keys();
		while (keys.hasNext()){
			String name = keys.next();
			bundle.putSerializable(name, (Serializable) object.get(name));
		}
		System.out.println(bundle.toString());
		assertEquals("com.eshel.currencyspirit", appContext.getPackageName());
	}
}
