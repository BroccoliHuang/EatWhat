package tw.broccoli.eatwhat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

public class MainActivity extends Activity {

    private ListView listview;
    private int[] images = new int[]{R.drawable.agnes, R.drawable.barney, R.drawable.bart, R.drawable.bart2, R.drawable.brokman, R.drawable.burns, R.drawable.comic_book_guy, R.drawable.flanders, R.drawable.grandpa, R.drawable.grandpa2, R.drawable.homer, R.drawable.jasper, R.drawable.lenny, R.drawable.lisa, R.drawable.lisa2, R.drawable.lovejoy, R.drawable.maggie, R.drawable.maggie_star, R.drawable.maggie2, R.drawable.marge, R.drawable.marge2, R.drawable.martin, R.drawable.milhouse, R.drawable.milhouse_face, R.drawable.milhouse2, R.drawable.moe, R.drawable.moe2, R.drawable.nelson, R.drawable.skinner, R.drawable.smithers, R.drawable.snowball, R.drawable.the_simpsons, R.drawable.whole_bart, R.drawable.whole_lisa, R.drawable.whole_maggie, R.drawable.whole_wiggum, R.drawable.wiggum, R.drawable.willie};
    private ArrayList<String> al_restaurant = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        al_restaurant = new ArrayList<>();
        al_restaurant.add("?角義大利麵");
        al_restaurant.add("夫妻麵攤");
        al_restaurant.add("鬍鬚張");
        al_restaurant.add("獅子");
        al_restaurant.add("黛西");
        al_restaurant.add("番茄");
        al_restaurant.add("拉亞");
        al_restaurant.add("小肉圓");
        al_restaurant.add("杏福小館");
        al_restaurant.add("老闆臉臭雞肉飯");
        al_restaurant.add("鵝肉飯");
        al_restaurant.add("肉圓");
        al_restaurant.add("藥膳");
        al_restaurant.add("午號出口");
        al_restaurant.add("手工麵");
        al_restaurant.add("韓國手卷");
        al_restaurant.add("花枝炒麵");
        al_restaurant.add("路易莎");
        al_restaurant.add("黑白切");
        al_restaurant.add("六六");
        al_restaurant.add("就是要吃早餐");
        al_restaurant.add("萬家鄉");
        al_restaurant.add("笑咪咪");
        al_restaurant.add("萬家鄉隔壁早餐店");
        al_restaurant.add("雙胞胎便當");
        al_restaurant.add("厝邊");
        al_restaurant.add("好粗");
        al_restaurant.add("翻桌");
        al_restaurant.add("奈野");
        al_restaurant.add("享樂時間");
        al_restaurant.add("IVY");
        al_restaurant.add("基隆廟口");
        al_restaurant.add("王漾");
        al_restaurant.add("麥麥");
        al_restaurant.add("麥麥隔壁早餐店");
        al_restaurant.add("草地人");
        al_restaurant.add("毅得");
        al_restaurant.add("毅得附近越南料理");
        al_restaurant.add("港式茶餐廳");
        al_restaurant.add("八方雲集");
        al_restaurant.add("四海遊龍");

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
                    listview.getChildAt(i).invalidate();
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 9999;
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
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            imageView.setImageResource(images[position % images.length]);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(al_restaurant.get(position % al_restaurant.size()));

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
