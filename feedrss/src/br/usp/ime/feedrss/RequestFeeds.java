package br.usp.ime.feedrss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

public class RequestFeeds extends AsyncTask<String, Void, List<JSONArray>> {
	
	@Override
	protected List<JSONArray> doInBackground(String... urls) {
		try {
			List<JSONArray> todosFeeds = new ArrayList<JSONArray>();
			for (int i = 0; i < urls.length; i++) {
				todosFeeds.add(getJson(urls[i]));
			}
			return todosFeeds;
		} catch (Exception e) {
			return null;
		}
	}

	private JSONArray getJson(String url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(builder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArray;
	}
}
