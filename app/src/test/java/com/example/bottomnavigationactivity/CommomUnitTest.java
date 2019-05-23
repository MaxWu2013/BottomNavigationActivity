package com.example.bottomnavigationactivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.bottomnavigationactivity.util.Commom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class CommomUnitTest {

    private final String URL = "/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f&limit=5";
    private final String URL_RESPONSE = "{\"help\": \"https://data.gov.sg/api/3/action/help_show?name=datastore_search\", \"success\": true, \"result\": {\"resource_id\": \"a807b7ab-6cad-4aa6-87d0-e283a7353a0f\", \"fields\": [{\"type\": \"int4\", \"id\": \"_id\"}, {\"type\": \"text\", \"id\": \"quarter\"}, {\"type\": \"numeric\", \"id\": \"volume_of_mobile_data\"}], \"records\": [{\"volume_of_mobile_data\": \"0.000384\", \"quarter\": \"2004-Q3\", \"_id\": 1}, {\"volume_of_mobile_data\": \"0.000543\", \"quarter\": \"2004-Q4\", \"_id\": 2}, {\"volume_of_mobile_data\": \"0.00062\", \"quarter\": \"2005-Q1\", \"_id\": 3}, {\"volume_of_mobile_data\": \"0.000634\", \"quarter\": \"2005-Q2\", \"_id\": 4}, {\"volume_of_mobile_data\": \"0.000718\", \"quarter\": \"2005-Q3\", \"_id\": 5}], \"_links\": {\"start\": \"/api/action/datastore_search?limit=5&resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f\", \"next\": \"/api/action/datastore_search?offset=5&limit=5&resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f\"}, \"limit\": 5, \"total\": 56}}";
    private final String ERROR_CONNECTION = "network error";

    @Mock
    Context mMockContext;

    @Mock
    ConnectivityManager mMockConManager;

    @Mock
    NetworkInfo mMockNetWorkInfo;

    /**
     * test httpGet when internet is conneted
     */
    @Test
    public void ensure_httpGet_isCorrect_when_internet_is_conneted() {

        //init MockServer
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(URL_RESPONSE));

        //return true when detect network connection
        when(mMockContext.getSystemService(mMockContext.CONNECTIVITY_SERVICE)).thenReturn( mMockConManager);
        when(mMockConManager.getActiveNetworkInfo()).thenReturn( mMockNetWorkInfo);
        when(mMockNetWorkInfo.isConnectedOrConnecting()).thenReturn(true);


        try{

            server.start();
            HttpUrl baseUrl = server.url(URL);
            String result = Commom.httpGet(URL,null,mMockContext);
            assertEquals(URL_RESPONSE,result);

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    /**
     * test httpGet when internet isn't connected
     */
    @Test
    public void ensure_httpGet_isCorrect_when_internet_not_conneted() {

        //init MockServer
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(URL_RESPONSE));

        //return true when detect network connection
        when(mMockContext.getSystemService(mMockContext.CONNECTIVITY_SERVICE)).thenReturn( mMockConManager);
        when(mMockConManager.getActiveNetworkInfo()).thenReturn( mMockNetWorkInfo);
        when(mMockNetWorkInfo.isConnectedOrConnecting()).thenReturn(false);


        try{

            server.start();
            HttpUrl baseUrl = server.url(URL);
            String result = Commom.httpGet(URL,null,mMockContext);
            assertEquals(ERROR_CONNECTION,result);

        }catch (Exception e){
            e.printStackTrace();
        }



    }



}