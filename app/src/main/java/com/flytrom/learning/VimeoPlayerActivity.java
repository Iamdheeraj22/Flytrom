package com.flytrom.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class VimeoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vimeo_player);

        String vimeoVideo = "<html><body><iframe width=\"420\" height=\"315\" src=\"https://player.vimeo.com/video/163996646?player_id=player\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
//        String vimeoVideo = "<html><body><iframe width=\"420\" height=\"315\" src=\"https://player.vimeo.com/video/562715241?player_id=player\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        String yourData = "<html>\n" +
                "<head>\n" +
                "  <title>{page_title}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "  <iframe src=\"https://player.vimeo.com/video/163996646\" width=\"{video_width}\" height=\"{video_height}\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>\n" +
                "\n" +
                "  <script src=\"https://player.vimeo.com/api/player.js\"></script>\n" +
                "  <script>\n" +
                "    <!- Your Vimeo SDK player script goes here ->\n" +
                "  </script>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        String dd = "<html>\n" +
                "<head>\n" +
                "  <title>{page_title}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "  <iframe src=\"https://player.vimeo.com/video/163996646\" width=\"{video_width}\" height=\"{video_height}\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>\n" +
                "\n" +
                "  <script src=\"https://player.vimeo.com/api/player.js\"></script>\n" +
                "  <script>\n" +
                "    var iframe = document.querySelector('iframe');\n" +
                "    var player = new Vimeo.Player(iframe);\n" +
                "\n" +
                "    player.on('play', function() {\n" +
                "      console.log('Played the video');\n" +
                "    });\n" +
                "\n" +
                "    player.getVideoTitle().then(function(title) {\n" +
                "      console.log('title:', title);\n" +
                "    });\n" +
                "  </script>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        WebView webView = (WebView) findViewById(R.id.myWebView);//	https://vimeo.com/562715241
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {

                webView.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(dd, "text/html", "utf-8");

    }
}