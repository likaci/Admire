package com.xiazhiri.admire.service;


import com.xiazhiri.admire.model.SideBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by liuwencai on 16/3/8.
 */
public class AdmireService {
    public static final String BASE_URL = "http://admire.so/";

    public static final String S_SIDE_BAR = "#tt > ul > a";
    public static final String S_WEB_CONTENT = "#container > li";


    public static Observable main() {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    List<SideBar> sideBarList = new ArrayList<>();
                    Document doc = Jsoup.connect(BASE_URL).get();

                    for (Element e : doc.select(S_SIDE_BAR)) {
                        SideBar sideBarElement = new SideBar(e.text(), e.attr("href"), e.child(0).attr("class"));
                        sideBarList.add(sideBarElement);
                    }

                    subscriber.onNext(sideBarList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

}
