package tw.broccoli.eatwhat.util;

import android.os.AsyncTask;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Broccoli on 2016/7/3.
 */
public class HttpRequest extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        try {
            String returnHtml = downloadUrl(params[0]);
            return returnHtml;
        }catch(IOException ie) {
            return "";
        }
    }

    private String downloadUrl(String urlString) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            String contentAsString = Jsoup.parse(is, "UTF-8", "https://spreadsheets.google.com/tq?key=1bjXUQ0vqsptfVCpnhEX9jSPAfc5qbE0fdvpwa-glmK0").text();
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
