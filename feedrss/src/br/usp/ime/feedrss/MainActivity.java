package br.usp.ime.feedrss;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        RequestFeeds requestFeeds = new RequestFeeds();
        requestFeeds.execute("http://www.ime.usp.br/index.php?option=com_eventlist&view=categoryevents&format=feed&id=62&type=rss");
        try {
			List<JSONArray> feedsPorCategoria = requestFeeds.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} 
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
