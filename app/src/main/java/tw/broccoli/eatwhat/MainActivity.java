package tw.broccoli.eatwhat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import tw.broccoli.eatwhat.util.HttpRequest;

public class MainActivity extends Activity {

    private ListView listview;
    private AlertDialog mAlertDialog = null;
//    private int[] images = new int[]{R.drawable.agnes, R.drawable.barney, R.drawable.bart, R.drawable.bart2, R.drawable.brokman, R.drawable.burns, R.drawable.comic_book_guy, R.drawable.flanders, R.drawable.grandpa, R.drawable.grandpa2, R.drawable.homer, R.drawable.jasper, R.drawable.lenny, R.drawable.lisa, R.drawable.lisa2, R.drawable.lovejoy, R.drawable.maggie, R.drawable.maggie_star, R.drawable.maggie2, R.drawable.marge, R.drawable.marge2, R.drawable.martin, R.drawable.milhouse, R.drawable.milhouse_face, R.drawable.milhouse2, R.drawable.moe, R.drawable.moe2, R.drawable.nelson, R.drawable.skinner, R.drawable.smithers, R.drawable.snowball, R.drawable.the_simpsons, R.drawable.whole_bart, R.drawable.whole_lisa, R.drawable.whole_maggie, R.drawable.whole_wiggum, R.drawable.wiggum, R.drawable.willie};
    private int[] images = new int[]{R.drawable.agnes, R.drawable.barney, R.drawable.brokman, R.drawable.burns, R.drawable.comic_book_guy, R.drawable.flanders, R.drawable.grandpa, R.drawable.grandpa2, R.drawable.jasper, R.drawable.lenny, R.drawable.lisa, R.drawable.lisa2, R.drawable.lovejoy, R.drawable.maggie, R.drawable.maggie2, R.drawable.marge, R.drawable.marge2, R.drawable.martin, R.drawable.milhouse, R.drawable.milhouse_face, R.drawable.milhouse2, R.drawable.moe, R.drawable.moe2, R.drawable.skinner, R.drawable.smithers, R.drawable.snowball, R.drawable.wiggum, R.drawable.willie};
    private ArrayList<String> al_restaurant = null;
    private boolean isNeedCircle = false;
    private String html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isOnline()){
            Toast.makeText(MainActivity.this, "要開網路哦", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        choiceDialog();
    }

    private void choiceDialog(){
        if(mAlertDialog!=null && mAlertDialog.isShowing()) return;
        try{
            if(html==null || html.equals("")) html = new HttpRequest().execute("https://spreadsheets.google.com/tq?key=1bjXUQ0vqsptfVCpnhEX9jSPAfc5qbE0fdvpwa-glmK0").get().replace("/*O_o*/ google.visualization.Query.setResponse(", "").replace(");", "");
            final JSONArray ja = new JSONObject(html).getJSONObject("table").getJSONArray("rows");

            ArrayList<String> category = new ArrayList<>();
            for(int temp=0;temp<ja.getJSONObject(0).getJSONArray("c").length();temp++){
                JSONObject jo = ja.getJSONObject(0).getJSONArray("c").optJSONObject(temp);
                if(jo!=null && !jo.getString("v").equals("null")) category.add(jo.getString("v"));
            }

            al_restaurant = new ArrayList<>();

            mAlertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("要吃哪個分類呢")
                    .setItems(category.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            try {
                                for(int count=1; count < ja.length(); count++) {
                                    JSONObject jo = ja.getJSONObject(count).getJSONArray("c").optJSONObject(which);
                                    if(jo!=null) al_restaurant.add(jo.getString("v"));
                                }

                                listview = (ListView) findViewById(R.id.lv);
                                listview.setAdapter(new MyAdapter());
                                listview.setClipToPadding(false);
                                listview.setClipChildren(false);
                                listview.setOnScrollListener(new OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                                    }

                                    @Override
                                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                        for (int i = 0; i < listview.getChildCount(); i++) {
                                            if (i == (int) (listview.getChildCount() / 2)) {
//                                                listview.getChildAt(i).setBackgroundColor(Color.argb(192, 244, 215, 45));
                                                listview.getChildAt(i).findViewById(R.id.donut).setVisibility(View.VISIBLE);
                                                ((TextView) listview.getChildAt(i).findViewById(R.id.text)).setTextColor(Color.rgb(0, 0, 0));
                                            } else {
//                                                listview.getChildAt(i).setBackgroundColor(Color.argb(0, 255, 255, 255));
                                                listview.getChildAt(i).findViewById(R.id.donut).setVisibility(View.GONE);
                                                ((TextView) listview.getChildAt(i).findViewById(R.id.text)).setTextColor(Color.rgb(216, 185, 12));
                                            }
                                            listview.getChildAt(i).invalidate();
                                        }
                                    }
                                });
                            }catch(JSONException je){
                                je.printStackTrace();
                            }
                        }
                    })
                    .setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            //press back
                            if(keyCode==4) onBackPressed();
                            return false;
                        }
                    })
                    .setCancelable(false)
                    .show();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }catch(JSONException je){
            je.printStackTrace();
        }catch(ExecutionException ee){
            ee.printStackTrace();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        if(mAlertDialog!=null && mAlertDialog.isShowing()){
            mAlertDialog.dismiss();
            finish();
        }else {
            listview.setAdapter(null);
            choiceDialog();
        }
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 999;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                MatrixView m = (MatrixView) LayoutInflater.from(MainActivity.this).inflate(R.layout.view_list_item, null);
                m.setParentHeight(listview.getHeight());
//                m.setBackgroundColor(Color.parseColor(randColor()));
                convertView = m;
            }
            CircleImageView circleImageView = (CircleImageView) convertView.findViewById(R.id.image);
            circleImageView.setImageResource(images[position % images.length]);
            if(isNeedCircle) {
                circleImageView.setBorderColor(Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            }else{
                circleImageView.setBorderWidth(0);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            if(al_restaurant!=null && al_restaurant.size()>0) textView.setText(al_restaurant.get(position % al_restaurant.size()));

            return convertView;
        }

    }

//    private String randColor(){
//        String returnColor = "#";
//        for(int count=0;count<6;count++){
//            int tempRnd = (int)(Math.random()*16);
//            returnColor = returnColor + (tempRnd>9?String.valueOf((char)(tempRnd-10+65)):String.valueOf(tempRnd));
//        }
//        return returnColor;
//    }

    @Deprecated
    public void changeGroupFlag(Object obj) throws Exception {
        Field[] f = obj.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredFields(); // 获得成员映射数组
        for (Field tem : f) {
            if (tem.getName().equals("mGroupFlags")) {
                tem.setAccessible(true);
                Integer mGroupFlags = (Integer) tem.get(obj);
                int newGroupFlags = mGroupFlags & 0xfffff8;
                tem.set(obj, newGroupFlags);
            }
        }
    }
}
