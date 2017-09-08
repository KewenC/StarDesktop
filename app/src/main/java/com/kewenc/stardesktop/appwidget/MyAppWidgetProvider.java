package com.kewenc.stardesktop.appwidget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.kewenc.stardesktop.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by KewenC on 2017/8/15.
 */

public class MyAppWidgetProvider extends AppWidgetProvider {

    //手机新浪
    private static final String SINA_URL="https://m.weibo.cn/u/1259110474?from=1077195010&wm=9848_0009&sourceType=weixin&uid=2902730223";
    private static final String YOUDAO_URL="http://dict.youdao.com/search?q=me&keyfrom=fanyi.smartResult";
    private static final String PC="http://weibo.com/210926262?c=spr_qdhz_bd_360ss_weibo_mr";
    private static final String Ido="https://idol001.com/star/zhaoliying.html";
    private static final String xx="https://m.weibo.cn/1259110474/4138636469611803";
    private static final String _360="https://www.so.com/s?q=赵丽颖的新浪微博";
    private String weibo_content=null;
    private int mIndex;
    private String[] route_links;
    private String[] route_titles;
    private String[] route_times;
    private String[] route_types;
    private Context context;
    String WeiboSite;
//    private Boolean isTop = false;
//    private Boolean isBottom = false;
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 1:
//
//            }
//        }
//    };
    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);
        this.context=context;
        Toast.makeText(context,"赵丽颖",Toast.LENGTH_SHORT).show();
        new MyAsyncTask().execute();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
                RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget);
                Intent intent_update = new Intent("stardesktop.appwidget.action.APPWIDGET_UPDATE");
                PendingIntent pendingIntent =PendingIntent.getBroadcast(context,1,intent_update,0);
                remoteViews.setOnClickPendingIntent(R.id.tv_name,pendingIntent);
                int route_count=ModifyHtml(MyHttpURLConnection(Ido));
                remoteViews.setViewVisibility(R.id.ll_route01,View.GONE);
                remoteViews.setViewVisibility(R.id.ll_route02,View.GONE);
                remoteViews.setViewVisibility(R.id.ll_route03,View.GONE);
                switch (route_count>3?3:route_count){
                    case 3:
                        Uri uri3=Uri.parse(route_links[2]);
                        Intent intent3=new Intent(Intent.ACTION_VIEW,uri3);
                        PendingIntent pendingIntent3=PendingIntent.getActivity(context,0,intent3,0);
                        remoteViews.setOnClickPendingIntent(R.id.ll_route03,pendingIntent3);
                        remoteViews.setViewVisibility(R.id.ll_route03,View.VISIBLE);
                        remoteViews.setTextViewText(R.id.tv_route_title03,"[3] "+route_titles[2]);
                        remoteViews.setTextViewText(R.id.tv_route_type03,route_times[2]+" | "+route_types[2]);
                    case 2:
                        Uri uri2=Uri.parse(route_links[1]);
                        Intent intent2=new Intent(Intent.ACTION_VIEW,uri2);
                        PendingIntent pendingIntent2=PendingIntent.getActivity(context,0,intent2,0);
                        remoteViews.setOnClickPendingIntent(R.id.ll_route02,pendingIntent2);
                        remoteViews.setViewVisibility(R.id.ll_route02,View.VISIBLE);
                        remoteViews.setTextViewText(R.id.tv_route_title02,"[2] "+route_titles[1]);
                        remoteViews.setTextViewText(R.id.tv_route_type02,route_times[1]+" | "+route_types[1]);
                    case 1:
                        Uri uri1=Uri.parse(route_links[0]);
                        Intent intent1=new Intent(Intent.ACTION_VIEW,uri1);
                        PendingIntent pendingIntent1=PendingIntent.getActivity(context,0,intent1,0);
                        remoteViews.setOnClickPendingIntent(R.id.ll_route01,pendingIntent1);
                        remoteViews.setViewVisibility(R.id.ll_route01,View.VISIBLE);
                        remoteViews.setTextViewText(R.id.tv_route_title01,"[1] "+route_titles[0]);
                        remoteViews.setTextViewText(R.id.tv_route_type01,route_times[0]+" | "+route_types[0]);
                }
//                InputStream inputStream=GetNenImage("https://tvax1.sinaimg.cn/crop.9.0.493.493.180/4b0c804aly8fig2bhbxm4j20e80dpq3f.jpg");
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                String tmp=MyHttpURLConnection("https://www.so.com/s?q=赵丽颖的微博");
//                String tmp=MyHttpURLConnection(_360);
//                Log.d("html","___________________________________________");
//                if (tmp!=null){
//                    Log.d("html:::::::::::::",tmp);
//                    LastWeiBo(context,tmp);
//                }else {
//                    weibo_content="访问失败！";
//                }
//
////                remoteViews.setImageViewBitmap(R.id.img,bitmap);
//                remoteViews.setTextViewText(R.id.tv_weibo,tmp);
////                remoteViews.setBitmap(R.id.tv_route,"setBackgroundbitmap",bitmap);
////                remoteViews.setTextViewText(R.id.tv_route_title01,route_content);
////                remoteViews.setViewVisibility(R.id.tv_route, View.GONE);
//                appWidgetManager.updateAppWidget(new ComponentName(context,MyAppWidgetProvider.class),remoteViews);

                Log.d("html","线程开始");
                while (weibo_content==null);
                Log.d("html","线程循环结束");
//                AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
//                RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget);
                Calendar calendar=Calendar.getInstance();
                String timeAuto=(calendar.get(Calendar.MONTH)+1)+"."+
                        calendar.get(Calendar.DAY_OF_MONTH)+"-"+
                        calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
                remoteViews.setTextViewText(R.id.tv_auto_time,timeAuto);
                remoteViews.setTextViewText(R.id.tv_weibo,weibo_content);
                appWidgetManager.updateAppWidget(new ComponentName(context,MyAppWidgetProvider.class),remoteViews);
            }
        }).start();
    }

    /**
     * 获取输入流
     * @param s
     * @return
     */
    private InputStream GetNenImage(String s) {
        try {
            URL url=new URL(s);
            HttpURLConnection http= (HttpURLConnection) url.openConnection();
            if ((http.getResponseCode()==HttpURLConnection.HTTP_OK)){
                return http.getInputStream();
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final int counter=appWidgetIds.length;
        for (int i=0;i<counter;i++){
            int appWidgetID=appWidgetIds[i];
            onWidgetUpdate(context,appWidgetManager,appWidgetID);
        }
    }

    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetID) {
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.widget);
        appWidgetManager.updateAppWidget(appWidgetID,remoteViews);
    }

    /**
     * HttpURLConnection
     * @param uri
     * @return
     */
    private String MyHttpURLConnection(String uri){
        try {
            String line ;
            StringBuilder content=new StringBuilder();
            URL url=new URL(uri);
            HttpURLConnection httpurlconnection= (HttpURLConnection) url.openConnection();
            if ((httpurlconnection.getResponseCode()==HttpURLConnection.HTTP_OK)){
//                    Toast.makeText(MainActivity.this,"连接成功！",Toast.LENGTH_SHORT).show();
                BufferedReader bufferedreader=new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
                while ((line=bufferedreader.readLine())!=null){
                    content.append(line);
                }
                bufferedreader.close();
                httpurlconnection.disconnect();
                return content.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
//                Toast.makeText(MainActivity.this,"访问失败！",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * html解析行程
     * @param s
     * @return
     */
    private int ModifyHtml(String s) {
        Log.d("html","ll:"+s);
        String content = "";
        org.jsoup.nodes.Document doc= Jsoup.parse(s);
        Element link=doc.select("ul").get(1);
        org.jsoup.nodes.Document doc1=Jsoup.parse(link.html());
        Elements links=doc1.select("a");
        route_links=new String[links.size()];
        route_titles=new String[links.size()];
        route_times=new String[links.size()];
        route_types=new String[links.size()];
        for (int i=0;i<links.size();i++){
            Element link_tmp=links.get(i);
            route_links[i]=link_tmp.attr("href");
            Elements links_tmp=link_tmp.select("p");
            for (int j=0;j<links_tmp.size();j++){
                Element link_tmp1=links_tmp.get(j);
                if (j==0){
                    String tmp=link_tmp1.text();
                    route_times[i]=tmp.length()<6?tmp:(tmp.substring(0,5)+" "+tmp.substring(5));
                }else if (j==1){
                    route_titles[i]=link_tmp1.text();
                }else {
                    route_types[i]=link_tmp1.text();
                }
            }
        }
//        return link.html();
        return links.size();
    }

    /**
     * html解析最新微博内容
     * @param s
     * @return
     */
    private String LastWeiBo(String s) {
//        Log.d("html:::",":::::");
//        Log.d("html:::",GetWeiBo(s));
//        WebViewGetContent(GetWeiBo(s));
//        String tmp=WebViewGetContent(GetWeiBo(s));
//        return tmp!=null?SimpleHtml(tmp):"访问失败！";
//        Log.d("html",GetWeiBo(s));
        return GetWeiBo(s);
//        return s;
    }

    /**
     * html解析获取最新微博地址
     * @param s
     * @return
     */
    private String GetWeiBo(String s) {
        org.jsoup.nodes.Document doc=Jsoup.parse(s);
        Element link=doc.select("section").get(1);
        org.jsoup.nodes.Document doc1=Jsoup.parse(link.html());
        Element link1=doc1.select("a").get(1);
        Log.d("html","最新的一条微博网址:"+link1.attr("href"));
        return link1.attr("href");
    }

    /**
     * 通过WebView获取数据
     * @param s
     * @return
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void WebViewGetContent(String s) {
        WebView wv=new WebView(context);
        try {
            wv.loadUrl(s);
            WebSettings settings=wv.getSettings();
            settings.setJavaScriptEnabled(true);
            wv.addJavascriptInterface(new InJavaScript(),"local_obj");
            wv.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl("javascript:window.local_obj.showSource(" +
                            "document.getElementsByTagName('body')[0].innerHTML);");
                    super.onPageFinished(view, url);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * JavaScriptInterface
     */
    private class InJavaScript {
        @JavascriptInterface
        public void showSource(String html){
            Log.d("html","微博内容源代码："+html);
            weibo_content=SimpleHtml(html);
//            Document doc=Jsoup.parse(html);
//            Element link_header=doc.select("html").first();
//            Log.d("html","最终微博解析内容："+weibo_content);
//            Log.d("html","微博解析内容："+link_header.html());
        }
    }

    /**
     * 解析详细内容
     * @param tmp
     * @return
     */
    private String SimpleHtml(String tmp) {
//        Log.i("html",tmp);
        String content;
        Document doc=Jsoup.parse(tmp);
        Element link_header=doc.select("header").first();
        Element link_img=link_header.select("img").first();
        Element link_h3=link_header.select("h3").first();
        Element link_h4=link_header.select("h4").first();
//        content=link_img.attr("src")+"\n"+link_h3.text()+"\n"+link_h4.text()+"\n";
        content=link_h4.text()+"\n";
        Element link_articl=doc.select("article").first();
        Elements links_div=link_articl.select("div");
        if (isWeiboRP(links_div)){
            Element link_weibo_text=link_articl.select("div").first();
            content+=link_weibo_text.text()+"\n"+links_div.get(mIndex).text()+"\n";
        }else{
            content+=link_articl.select("div").first().text();
        }


//        Element link_weibo_text=link_articl.select("div").first();
//        Elements link_spans=link_articl.select("span");
//        String spans = "";
//        for (int i=0;i<link_spans.size();i++){
//            spans+=link_spans.get(i).text();
//        }
//        content+=link_weibo_text.text()+"\n"+spans;
        return content;
    }

    /**
     * 判断是否存在转载
     * @param links_div
     * @return
     */
    private boolean isWeiboRP(Elements links_div) {
        for (int i=0;i<links_div.size();i++){
            if (links_div.get(i).attr("class").equals("weibo-rp")){
                mIndex=i;
                return true;
            }
        }
        return false;
    }

    /**
     * MyAsyncTask
     */
    private class MyAsyncTask extends android.os.AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String weibo_site = null;
            String tmp=MyHttpURLConnection("https://www.so.com/s?q=赵丽颖的微博");
            if (tmp!=null){
                weibo_site=LastWeiBo(tmp);
            }else {
                    weibo_content="访问失败！";
            }
            return weibo_site;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            WeiboSite=s;
            WebViewGetContent(WeiboSite);
            Log.d("html","onPostExecute异步加载完成");
//            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
//            RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget);
//            remoteViews.setTextViewText(R.id.tv_weibo,s);
//            appWidgetManager.updateAppWidget(new ComponentName(context,MyAppWidgetProvider.class),remoteViews);
        }


    }
}
